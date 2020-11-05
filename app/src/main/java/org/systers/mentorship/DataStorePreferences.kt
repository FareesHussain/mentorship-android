package org.systers.mentorship

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStorePreferences(context: Context) {
    private val dataStore = context.createDataStore(name = "preferences")

    val firstRun = context.getString(R.string.intro_prefs_first_run)

    companion object{
        val IS_FIRST_RUN = preferencesKey<Boolean>("intro_prefs_key")
    }
    suspend fun stopIntro() {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_RUN] = false
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
}