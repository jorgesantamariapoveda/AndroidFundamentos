package org.jsantamariap.eh_ho.data

import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.jsantamariap.eh_ho.BuildConfig
import org.json.JSONObject

/*
Petición custom para realizar las peticiones.

Detalle importante sobre los parámetros de la clase. Únicamente username es una propiedad
de la clase, el resto son los parámetros necesarios para satisfacer el constructor de la clase
JsonObjectRequest que dicha clase hereda
 */

class PostRequest(
    method: Int,
    url: String,
    body: JSONObject?,
    private val username: String? = null,
    listener: (response: JSONObject?) -> Unit,
    errorListener: (errorResponse: VolleyError) -> Unit
) : JsonObjectRequest(
    method, url, body, listener, errorListener
) {
    override fun getHeaders(): MutableMap<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"
        headers["Api-Key"] = BuildConfig.DiscourseApiKey

        username?.let {
            headers["Api-username"] = it
        }

        return headers
    }
}