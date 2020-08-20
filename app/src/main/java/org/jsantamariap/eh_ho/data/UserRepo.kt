package org.jsantamariap.eh_ho.data

import android.content.Context
import com.android.volley.NetworkError
import com.android.volley.Request
import com.android.volley.ServerError
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.jsantamariap.eh_ho.BuildConfig
import org.jsantamariap.eh_ho.R

const val PREFERENCES_SESSION = "session"
const val PREFERENCES_USERNAME = "username"

object UserRepo {

    // mediante el paso de dos lambdas como success y error
    // podemos realizar notificaciones para que sea la actividad
    // la que realice el trabajo
    fun signIn(
        context: Context,
        signInModel: SignInModel,
        success: (SignInModel) -> Unit,
        error: (RequestError) -> Unit
    ) {
        // 1. crear request
        // primera forma, antes de haber creado el ApiRouter
        /*
        val request = JsonObjectRequest(
            Request.Method.GET,
            "https://mdiscourse.keepcoding.io/users/${signInModel.username}.json",
            null,
            { response ->
                // 5. notificar que la petición fu exitosa
                success(signInModel)
                // persistencia
                saveSession(
                    context,
                    signInModel.username
                )
            },
            { err ->
                err.printStackTrace()
                error(err)
            }
        )
        */
        // segunda forma, después de haber creado el ApiRouter
        val request = JsonObjectRequest(
            Request.Method.GET,
            ApiRoutes.signIn(signInModel.username),
            null,
            { response ->
                // 5. notificar que la petición fu exitosa
                success(signInModel)
                // persistencia
                saveSession(
                    context,
                    signInModel.username
                )
            },
            { err ->
                err.printStackTrace()

                // para que quede más limpio asignamos var al if (ver más abajo)
                /*
                if (err is ServerError && err.networkResponse.statusCode == 404) {
                    val errorObject = RequestError(err, messageResId = R.string.error_not_registered)
                    error(errorObject)
                } else if (err is NetworkError) {
                    val errorObject = RequestError(err, messageResId = R.string.error_not_internet)
                    error(errorObject)
                } else {
                    val errorObject = RequestError(err)
                    error(err)
                }
                 */
                var errorObject = if (err is ServerError && err.networkResponse.statusCode == 404) {
                    RequestError(err, messageResId = R.string.error_not_registered)
                } else if (err is NetworkError) {
                    RequestError(err, messageResId = R.string.error_not_internet)
                } else {
                    RequestError(err)
                }

                error(errorObject)
            }
        )

        // 2. encolar petición
        // antes de haber hecho el "singleton" de la cola
        /*
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
         */
        // después del singleton
        ApiRequestQueue.getRequestQueue(context)
            .add(request)

        // 3.- otorgar permisos internet -> manifest.xml
    }

    fun signUp(
        context: Context,
        signUpModel: SignUpModel,
        success: (SignUpModel) -> Unit,
        error: (RequestError) -> Unit
    ) {
        val request = PostRequest(
            Request.Method.POST,
            ApiRoutes.signUp(),
            signUpModel.toJson(),
            null,
            { response ->
                // a veces ocurre que auqneu el servidor devuelva un 200 puede haber algún
                // error (normalmente de formateo). Por lo que también es interesante consultar
                // los campos que pueda devolver el body (se pueden ver x.ej desde Postman
                if (response?.getBoolean("success") == true) {
                    success(signUpModel)
                }
                else {
                    error(RequestError(message = response?.getString("message")))
                }
            },
            { e ->
                e.printStackTrace()

                val requestError =
                    if (e is NetworkError)
                        RequestError(e, messageResId = R.string.error_not_internet)
                    else
                        RequestError(e)

                error(requestError)
            }
        )

        ApiRequestQueue
            .getRequestQueue(context)
            .add(request)
    }

    private fun saveSession(context: Context, username: String) {
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

    fun getUsername(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFERENCES_SESSION, Context.MODE_PRIVATE)

        return preferences.getString(PREFERENCES_USERNAME, null)
    }
}