package com.example.bolna.core

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Creates one DataStore file named "auth_prefs", attached to Context
private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

@Singleton
class TokenStorage @Inject constructor(
    @ApplicationContext context: Context) {

    private val appContext = context.applicationContext
    private val tokenKey = stringPreferencesKey("auth_token")

    // A stream of the saved token: emits null if nothing is saved
    val tokenFlow: Flow<String?> = appContext.dataStore.data.map { prefs -> prefs[tokenKey] }

    suspend fun saveToken(token: String) {
        appContext.dataStore.edit { prefs -> prefs[tokenKey] = token }
    }

    suspend fun clearToken() {
        appContext.dataStore.edit { prefs -> prefs.remove(tokenKey) }
    }
}