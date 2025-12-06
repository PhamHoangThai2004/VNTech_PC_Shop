package com.pht.vntechpc.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.pht.vntechpc.domain.model.User
import kotlinx.coroutines.flow.first

class UserPreferences(private val context: Context) {
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER = stringPreferencesKey("user")
    }

    suspend fun getAccessToken() = context.userPreferences.data.first()[ACCESS_TOKEN]

    suspend fun getRefreshToken() = context.userPreferences.data.first()[REFRESH_TOKEN]

    suspend fun saveAccessToken(token: String) {
        context.userPreferences.edit {
            it[ACCESS_TOKEN] = token
        }
    }

    suspend fun saveRefreshToken(token: String) {
        context.userPreferences.edit {
            it[REFRESH_TOKEN] = token
        }
    }

    suspend fun saveUser(user: User) {
        val json = Gson().toJson(user)
        context.userPreferences.edit {
            it[USER] = json
        }
    }

    suspend fun getUser(): User? {
        val json = context.userPreferences.data.first()[USER]
        return Gson().fromJson(json, User::class.java)
    }

    suspend fun clear() {
        context.userPreferences.edit { preferences ->
            preferences.clear()
        }
    }
}

val Context.userPreferences: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")