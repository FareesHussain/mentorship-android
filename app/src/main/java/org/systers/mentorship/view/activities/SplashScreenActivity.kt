package org.systers.mentorship.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.systers.mentorship.utils.DataStorePreferencesManager
import org.systers.mentorship.R
import java.lang.Runnable

/**
 * This activity will show the organisation logo for sometime and then start the next activity
 */
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var SPLASH_DISPLAY_LENGTH: Long = 1000
    private val dataStorePreferencesManager = DataStorePreferencesManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var firstRun : Boolean
        runBlocking {
            firstRun = DataStorePreferencesManager().firstRunFlow.first()
        }

        if (firstRun) {
            startActivity(Intent(this, IntroActivity::class.java))
        } else {
            var authToken : String
            runBlocking {
                authToken = dataStorePreferencesManager.authTokenFlow.first()
            }
            val intent = if (authToken.isEmpty()) {
                Intent(this, LoginActivity::class.java)
            } else {
                Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }



        runnable = Runnable {
            startActivity(intent)
            finish()
        }
        handler = Handler()
        handler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH)
    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacks(runnable)
    }
}
