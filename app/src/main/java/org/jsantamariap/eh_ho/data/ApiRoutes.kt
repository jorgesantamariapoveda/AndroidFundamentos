package org.jsantamariap.eh_ho.data

import android.net.Uri

object ApiRoutes {

    fun signIn(username: String) =
        uriBuilder()
            .appendPath("users")
            .appendPath("${username}.json")
            .build()
            .toString()

    private fun uriBuilder() =
        Uri.Builder()
            .scheme("https")
            .authority("mdiscourse.keepcoding.io")
}