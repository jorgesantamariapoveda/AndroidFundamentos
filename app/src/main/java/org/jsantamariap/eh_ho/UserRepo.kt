package org.jsantamariap.eh_ho

import android.content.Context

const val PREFERENCES_SESSION = "session"
const val PREFERENCES_USERNAME = "username"

object UserRepo {

    fun signIn(context: Context, username: String) {
        val preferences = context.getSharedPreferences(PREFERENCES_SESSION, Context.MODE_PRIVATE)

        preferences.edit()
            .putString(PREFERENCES_USERNAME, username)
            .apply()
    }

    fun logout(context: Context) {
        val preferences = context.getSharedPreferences(PREFERENCES_SESSION, Context.MODE_PRIVATE)

        preferences.edit()
            .putString(PREFERENCES_USERNAME, null)
            .apply()
    }

    fun isLogged(context: Context): Boolean {
        val preferences = context.getSharedPreferences(PREFERENCES_SESSION, Context.MODE_PRIVATE)

        val username = preferences.getString(PREFERENCES_USERNAME, null)
        return username != null
    }
}