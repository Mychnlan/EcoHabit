package com.team4.ecohabit.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "user_session"
)

class SessionManager(
    private val context: Context
) {

    companion object {

        val IS_LOGGED_IN =
            booleanPreferencesKey("is_logged_in")

        val LOGIN_TIME =
            longPreferencesKey("login_time")
    }

    suspend fun saveLogin() {

        context.dataStore.edit { pref ->

            pref[IS_LOGGED_IN] = true

            pref[LOGIN_TIME] =
                System.currentTimeMillis()
        }
    }

    suspend fun logout() {

        context.dataStore.edit { pref ->

            pref.clear()
        }
    }

    fun isLoginValid(): Flow<Boolean> {

        return context.dataStore.data.map { pref ->

            val isLoggedIn =
                pref[IS_LOGGED_IN] ?: false

            val loginTime =
                pref[LOGIN_TIME] ?: 0L

            val oneWeek =
                7 * 24 * 60 * 60 * 1000L

            val isExpired =
                System.currentTimeMillis() - loginTime > oneWeek

            isLoggedIn && !isExpired
        }
    }

    suspend fun getLoginState(): Boolean {

        val pref = context.dataStore.data.first()

        val isLoggedIn =
            pref[IS_LOGGED_IN] ?: false

        val loginTime =
            pref[LOGIN_TIME] ?: 0L

        val oneWeek =
            7 * 24 * 60 * 60 * 1000L

        val isExpired =
            System.currentTimeMillis() - loginTime > oneWeek

        return isLoggedIn && !isExpired
    }
}
