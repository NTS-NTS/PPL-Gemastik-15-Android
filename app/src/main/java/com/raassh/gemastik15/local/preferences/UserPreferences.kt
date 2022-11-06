package com.raassh.gemastik15.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    fun getHasDisabilityTypes() = dataStore.data.map { preferences ->
        preferences[HAS_DISABILITY_TYPES_KEY]
    }

    suspend fun setHasDisabilityTypes(hasDisabilityTypes: Boolean) {
        dataStore.edit { preferences ->
            preferences[HAS_DISABILITY_TYPES_KEY] = hasDisabilityTypes
        }
    }

    companion object {
        val TOKEN_KEY = stringPreferencesKey("token")
        val HAS_DISABILITY_TYPES_KEY = booleanPreferencesKey("has_disability_types")
    }
}