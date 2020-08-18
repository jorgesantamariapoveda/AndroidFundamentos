package org.jsantamariap.eh_ho.data

import com.android.volley.VolleyError

class RequestError (
    val volleyError: VolleyError?,
    val message: String? = null,
    // identificador de un string de los recursos R
    val messageResId: Int? = null //R.string.····
)