package org.jsantamariap.eh_ho.data

import android.content.Context
import com.android.volley.NetworkError
import com.android.volley.Request
import com.android.volley.ServerError
import com.android.volley.toolbox.JsonObjectRequest
import org.jsantamariap.eh_ho.R

const val PREFERENCES_SESSION = "session"
const val PREFERENCES_USERNAME = "username"

/*
object: clase anómina, viene a ser un singleton. Es decir una única instancia de una clase, teniendo su propio
contexto estático.
 */
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
        val request = JsonObjectRequest(
            Request.Method.GET,
            ApiRoutes.signIn(signInModel.username),
            null,
            { response ->
                // notificar que la petición fue exitosa
                success(signInModel)
                // persistencia
                saveSession(
                    context,
                    signInModel.username
                )
            },
            { err ->
                err.printStackTrace()

                // para que quede más limpio asignamos var al if
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
                // a veces ocurre que aunque el servidor devuelva un 200 puede haber algún
                // error (normalmente de formateo). Por lo que también es interesante consultar
                // los campos que pueda devolver el body (se pueden ver x.ej desde Postman).
                // Es decir, en este caso "success" es porque lo hemos visto en Postman
                if (response?.getBoolean("success") == true) {
                    success(signUpModel)
                }
                else {
                    // Ídem en este caso, "message" es porque lo hemos visto en Postman
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

    private fun saveSession(context: Context, username: String) {
        val preferences = context.getSharedPreferences(PREFERENCES_SESSION, Context.MODE_PRIVATE)

        preferences.edit()
            .putString(PREFERENCES_USERNAME, username)
            .apply()
    }
}