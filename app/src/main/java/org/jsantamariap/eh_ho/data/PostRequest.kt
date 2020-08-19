package org.jsantamariap.eh_ho.data

import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

// petición custom para añadir los headers
class PostRequest(
    method: Int,
    url: String,
    body: JSONObject?,
    listener: (response: JSONObject?) -> Unit,
    errorListener: (errorResponse: VolleyError) -> Unit
) : JsonObjectRequest(
    method, url, body, listener, errorListener
) {
    override fun getHeaders(): MutableMap<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"

        return headers
    }


}