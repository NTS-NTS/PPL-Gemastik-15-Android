package com.raassh.gemastik15.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {
    fun getToken() = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    suspend fun setToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    companion object {
        val TOKEN_KEY = stringPreferencesKey("token")
    }
}