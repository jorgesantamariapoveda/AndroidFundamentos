package org.jsantamariap.eh_ho.data

import android.net.Uri
import org.jsantamariap.eh_ho.BuildConfig

/*
object: clase anómina, viene a ser un singleton. Es decir una única instancia de una clase, teniendo su propio
contexto estático.

Todas las funciones descritas a continuación son funciones inline
 */

object ApiRoutes {

    fun signIn(username: String) =
        uriBuilder()
            .appendPath("users")
            .appendPath("${username}.json")
            .build()
            .toString()

    fun signUp() =
        uriBuilder()
            .appendPath("users")
            .build()
            .toString()

    fun getTopics() =
        uriBuilder()
            .appendPath("latest.json")
            .build()
            .toString()

    fun createTopic() =
        uriBuilder()
            .appendPath("posts.json")
            .build()
            .toString()

    private fun uriBuilder() =
        Uri.Builder()
            .scheme("https")
            .authority(BuildConfig.DiscourseDomain)
}