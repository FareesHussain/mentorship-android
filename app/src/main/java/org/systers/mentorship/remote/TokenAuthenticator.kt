package org.systers.mentorship.remote

import android.content.Intent
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.utils.Constants
import org.systers.mentorship.utils.DataStorePreferencesManager
import org.systers.mentorship.view.activities.LoginActivity

class TokenAuthenticator: Authenticator{

    private val dataStorePreferencesManager = DataStorePreferencesManager()

    override fun authenticate(route: Route, response: Response): Request? {
        if (response.code() == 401) {
            CoroutineScope(Dispatchers.IO).launch { dataStorePreferencesManager.clear() }
            val intent = Intent(MentorshipApplication.getContext(), LoginActivity::class.java)
            intent.putExtra(Constants.TOKEN_EXPIRED_EXTRA, 0)
            ContextCompat.startActivity(MentorshipApplication.getContext(), intent, null)
        }
        return response.request().newBuilder().build()
    }
}
