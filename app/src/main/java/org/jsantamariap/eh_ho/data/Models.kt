package org.jsantamariap.eh_ho.data

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

// MARK: - Topics

data class Topic(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val date: Date = Date(),
    val posts: Int = 0,
    val views: Int = 0
) {
    // al colocar 1000L estamos diciendo que sea un Long para ampliar el rango y que no halla un desbordamiento
    val MINUTE_MILLIS = 1000L * 60
    val HOUR_MILLIS = MINUTE_MILLIS * 60
    val DAY_MILLIS = HOUR_MILLIS * 24
    val MONTH_MILLIS = DAY_MILLIS * 30
    val YEAR_MILLIS = MONTH_MILLIS * 12

    // No hay ningún problema que dentro de un data class se declare otro data class
    data class TimeOffSet(
        val amount: Int,
        val unit: Int
    )

    // parámetro con valor por defecto
    fun getTimeOffSet(dateToCompare: Date = Date()): TimeOffSet {
        val currentDate = dateToCompare.time
        val difference = currentDate - this.date.time

        val years = difference / YEAR_MILLIS
        if (years > 0) return TimeOffSet(
            years.toInt(),
            Calendar.YEAR
        )

        val months = difference / MONTH_MILLIS
        if (months > 0) return TimeOffSet(
            months.toInt(),
            Calendar.MONTH
        )

        val days = difference / DAY_MILLIS
        if (days > 0) return TimeOffSet(
            days.toInt(),
            Calendar.DAY_OF_MONTH
        )

        val hours = difference / HOUR_MILLIS
        if (hours > 0) return TimeOffSet(
            hours.toInt(),
            Calendar.HOUR
        )

        val minutes = difference / MINUTE_MILLIS
        if (minutes > 0) return TimeOffSet(
            minutes.toInt(),
            Calendar.MINUTE
        )

        return TimeOffSet(0, Calendar.MINUTE)
    }

    /*
    El uso de companion object es para declarar un ámbito donde todas las funciones
    tienen el alcance de estáticas
     */
    companion object {

        fun parseTopicList(response: JSONObject): List<Topic> {
            val objectLis = response.getJSONObject("topic_list")
                .getJSONArray("topics")

            val topics = mutableListOf<Topic>()

            /*
            lo ideal sería hacer un map, pero resulta que no lo está implementado
            en los jsonObject, por lo que hay que hacerlo con un for típico
             */
            for (i in 0 until objectLis.length()) {
                val parsedTopic = parseTopic(objectLis.getJSONObject(i))
                topics.add(parsedTopic)
            }

            return topics
        }

        fun parseTopic(jsonObject: JSONObject): Topic {
            // java no puede tratar el tema de las zonas horarios Z
            // ej 2020-08-13T16:27:00.945Z, entonces vamos a eliminarla
            val date = jsonObject.getString("created_at")
                .replace("Z", "+0000")

            // SSSZ -> zona horaria
            // El locale es la zona local a la cual se va a convertir
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
            val dateFormatted = dateFormat.parse(date) ?: Date()

            return Topic(
                id = jsonObject.getInt("id").toString(),
                title = jsonObject.getString("title"),
                date = dateFormatted,
                posts = jsonObject.getInt("posts_count"),
                views = jsonObject.getInt("views")
            )
        }
    }
}

// MARK: - Posts

data class Post(
    val author: String,
    val content: String,
    val date: Date = Date()
) {
    companion object {

        fun parseTopicList(response: JSONObject): List<Post> {
            val objectLis = response.getJSONObject("post_stream")
                .getJSONArray("posts")

            val posts = mutableListOf<Post>()

            /*
            lo ideal sería hacer un map, pero resulta que no lo está implementado
            en los jsonObject, por lo que hay que hacerlo con un for típico
             */
            for (i in 0 until objectLis.length()) {
                val parsedPost = parsePost(objectLis.getJSONObject(i))
                posts.add(parsedPost)
            }

            return posts
        }

        fun parsePost(jsonObject: JSONObject): Post {
            // java no puede tratar el tema de las zonas horarios Z
            // ej 2020-08-13T16:27:00.945Z, entonces vamos a eliminarla
            val date = jsonObject.getString("created_at")
                .replace("Z", "+0000")

            // SSSZ -> zona horaria
            // El locale es la zona local a la cual se va a convertir
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
            val dateFormatted = dateFormat.parse(date) ?: Date()

            return Post(
                author = jsonObject.getString("username").toString(),
                content = jsonObject.getString("cooked"),
                date = dateFormatted
            )
        }

    }
}
