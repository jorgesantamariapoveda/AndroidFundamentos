package org.jsantamariap.eh_ho.data

import com.android.volley.VolleyError

/*
Clase con tres propiedades creadas en el mismo momento de instanciarla. Es el azúcar sintáctico
de kotlin
 */

class RequestError (
    val volleyError: VolleyError? = null,
    val message: String? = null,
    // identificador de un string de los recursos R
    val messageResId: Int? = null //R.string.····
)