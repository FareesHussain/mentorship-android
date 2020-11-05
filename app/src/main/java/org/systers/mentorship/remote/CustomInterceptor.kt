package org.systers.mentorship.remote

import androidx.annotation.NonNull
import android.text.TextUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import org.systers.mentorship.utils.DataStorePreferencesManager

/**
 * Represents a custom HTTP requests interceptor
 */
class CustomInterceptor: Interceptor {

    val datastorePreferenceManager = DataStorePreferencesManager()

    override fun intercept(@NonNull chain: Interceptor.Chain): Response {

        val chainRequest = chain.request()
        val builder = chainRequest.newBuilder()

        CoroutineScope(Dispatchers.IO).launch{
            val accessToken = datastorePreferenceManager.authTokenFlow.first()
            if (!TextUtils.isEmpty(accessToken)) {
                builder.header("Authorization", accessToken)
            }
        }
        val request = builder.build()
        return chain.proceed(request)
    }
}
