package org.jsantamariap.eh_ho.data

import org.json.JSONObject

// Cuando veamos en la parte avanzada retrofit
// se usaran adaptadores para hacer el parseo de forma
// automática. Es importante saber la forma manual porque hay casos que solamente
// puede hacerse así.

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

data class CreateTopicModel (
    val title: String,
    val content: String
) {
    fun toJson(): JSONObject {
        return JSONObject()
            .put("title", title)
            .put("raw", content)
    }
}
