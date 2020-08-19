package org.jsantamariap.eh_ho.data

import org.json.JSONObject

data class SignInModel(
    val username: String,
    val password: String
)

data class SignUpModel(
    val username: String,
    val email: String,
    val password: String
) {
    fun toJson(): JSONObject {
        return JSONObject() // "{}"
            .put("name", username)
            .put("username", username)
            .put("password", password)
            .put("email", email)
            .put("active", true)
            .put("approved", true)
    }
}