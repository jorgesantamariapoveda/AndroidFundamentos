package org.jsantamariap.eh_ho.data

import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.jsantamariap.eh_ho.BuildConfig
import org.json.JSONObject

// petición custom para añadir los headers
// el parámetro username es para el createTopic
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

        // forma 1
        /*
        if (username != null) {
            headers["Api-username"] = username
        }
         */
        // forma 2
        username?.let {
            headers["Api-username"] = it
        }

        return headers
    }
}