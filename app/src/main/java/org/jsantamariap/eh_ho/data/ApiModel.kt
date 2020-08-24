package org.jsantamariap.eh_ho.data

import org.json.JSONObject

/*
En la parte avanzada de Android se verá que para el parseo nos podemos ayudar de la librería
Retrofit. No obstante, es importante saber la forma de hacerlo manualmente, tal y como aquí
se ha hecho porque hay casos que no solamente se podrá hacer así.

data class: nos ahorra mucho trabajo, se usa para que kotlin genere de forma automática toda
los setters y getters por nosotros. Eso no impide que la dicha clase pueda tener sus propios
métodos. Bye a bye a los POJO (acrónimo de Plain Old Java Object)
*/

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
