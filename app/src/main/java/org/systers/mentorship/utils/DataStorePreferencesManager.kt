package org.systers.mentorship.utils

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import java.io.IOException

class DataStorePreferencesManager() {

    val context = MentorshipApplication.getContext()

    private val dataStore = context.createDataStore(name = "preferences")
    private val authDataStore = context.createDataStore(name = "auth-preferences")

    companion object{
        val IS_FIRST_RUN = preferencesKey<Boolean>("intro_prefs_key")
        val AUTH_TOKEN = preferencesKey<String>("auth-token")
    }
    suspend fun stopIntro() {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_RUN] = false
        }
    }

    suspend fun putAuthToken(authToken : String){
        authDataStore.edit { preference ->
            preference[AUTH_TOKEN] = "Bearer $authToken"
        }
    }

    suspend fun clear(){
        authDataStore.edit {
            clear()
        }
    }

    val firstRunFlow : Flow<Boolean> = dataStore.data
            .catch {
                if(it is IOException){
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map {preference ->
                val isFirstRun = preference[IS_FIRST_RUN]?:true
                isFirstRun
            }

    val authTokenFlow : Flow<String> = authDataStore.data
            .catch {
                if(it is IOException){
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map {preference ->
                val authToken = preference[AUTH_TOKEN]?:""
                authToken
            }
}