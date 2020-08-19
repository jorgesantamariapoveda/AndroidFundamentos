package org.jsantamariap.eh_ho.data

import android.content.Context
import com.android.volley.NetworkError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.jsantamariap.eh_ho.R

// object se usa para patrón de diseño Singleton
object TopicsRepo {
    val topics: MutableList<Topic> = mutableListOf()
    // get custom, justamente debajo de la propiedad (por el uso de field)
    // si se usa directamente topics se produce un stackoverflow, recursiva

    // borrado una vez que se implementó el método addTopic
    /*
    get() {
        if (field.isEmpty())
            field.addAll(createDummyTopics())
        return field
    }
     */


    // función pura
    /*
    fun createDummyTopics(count: Int = 10): List<Topic> {
        /*
        // forma tradicional
        val dummies = mutableListOf<Topic>()

        for (i in 0..count) {
            val topic: Topic = Topic(
                title = "Topic $i",
                content = "Content $i"
            )
            dummies.add(topic)
        }

        return dummies
         */

        return (0..count).map {
            Topic(
                title = "Topic $it",
                content = "Content $it"
            )
        }
    }
     */

    // función inline
    // variante sin saber tipo que devuelve
    // fun createDummyTopics(count: Int = 10) =
    fun createDummyTopics(count: Int = 20): List<Topic> =
        (0..count).map {
            Topic(
                title = "Topic $it"
            )
        }

    // otro uso de una funcion inline
    fun getTopic(id: String): Topic? = topics.find {
        it.id == id
    }

    fun addTopic(title: String) {
        val topic =
            Topic(title = title)
        topics.add(topic)
    }

    fun getTopics(
        context: Context,
        onSuccess: (List<Topic>) -> Unit,
        onError: (RequestError) -> Unit
    ) {
        val request = JsonObjectRequest(
            Request.Method.GET,
            ApiRoutes.getTopics(),
            null,
            {
                val list = Topic.parseTopicList(it)
                onSuccess(list)
            },
            {
                it.printStackTrace()
                val requestError =
                    if (it is NetworkError)
                        RequestError(it, messageResId = R.string.error_not_internet)
                    else
                        RequestError(it)
                onError(requestError)
            }
        )

        ApiRequestQueue
            .getRequestQueue(context)
            .add(request)
    }
}