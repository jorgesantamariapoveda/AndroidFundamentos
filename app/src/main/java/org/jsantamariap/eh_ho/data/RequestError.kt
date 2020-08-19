package org.jsantamariap.eh_ho.data

import com.android.volley.VolleyError

class RequestError (
    val volleyError: VolleyError? = null, // al final se puso anull por defecto porque en el
    // caso del post pudiera ser que no hubiese error de Volly pero si que quiséramos sacar
    // un error
    val message: String? = null,
    // identificador de un string de los recursos R
    val messageResId: Int? = null //R.string.····
)