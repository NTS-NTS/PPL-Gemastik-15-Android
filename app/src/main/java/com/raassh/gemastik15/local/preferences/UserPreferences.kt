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

    fun getName() = dataStore.data.map { preferences ->
        preferences[NAME_KEY]
    }

    suspend fun setName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
        }
    }

    fun getUserName() = dataStore.data.map { preferences ->
        preferences[USERNAME_KEY]
    }

    suspend fun setUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = userName
        }
    }

    fun getIsModerator() = dataStore.data.map { preferences ->
        preferences[IS_MODERATOR_KEY]
    }

    suspend fun setIsModerator(isModerator: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_MODERATOR_KEY] = isModerator
        }
    }

    fun getIsBanned() = dataStore.data.map { preferences ->
        preferences[IS_BANNED_KEY]
    }

    suspend fun setIsBanned(isBanned: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_BANNED_KEY] = isBanned
        }
    }

    fun getIsVerified() = dataStore.data.map { preferences ->
        preferences[IS_VERIFIED_KEY]
    }

    suspend fun setIsVerified(isVerified: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_VERIFIED_KEY] = isVerified
        }
    }

    companion object {
        val TOKEN_KEY = stringPreferencesKey("token")
        val HAS_DISABILITY_TYPES_KEY = booleanPreferencesKey("has_disability_types")
        val NAME_KEY = stringPreferencesKey("name")
        val USERNAME_KEY = stringPreferencesKey("username")
        val IS_MODERATOR_KEY = booleanPreferencesKey("is_moderator")
        val IS_BANNED_KEY = booleanPreferencesKey("is_banned")
        val IS_VERIFIED_KEY = booleanPreferencesKey("is_verified")
    }
}