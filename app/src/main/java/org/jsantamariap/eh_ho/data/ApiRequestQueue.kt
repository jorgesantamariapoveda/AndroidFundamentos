package org.jsantamariap.eh_ho.data

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/*
object: clase anómina, viene a ser un singleton. Es decir una única instancia de una clase, teniendo su propio
contexto estático.

Volley: uso de una librería para lo relacionado con las peticiones a una api internet. Gestiona
automáticamente una cola de peticiones. Se basa en dos threads (uno en cache o otro para network)
En el momento de encolar las peticiones con el método add se ejecuta la misma.

Por cierto, para poder realizar peticiones a internet hay que que dar permisos a Android, ello
se realiza en el archivo AndroidManifiest.xml
 */

object ApiRequestQueue {

    private var requestQueue: RequestQueue? = null

    fun getRequestQueue(context: Context): RequestQueue {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context)
        }

        return requestQueue as RequestQueue
    }
}