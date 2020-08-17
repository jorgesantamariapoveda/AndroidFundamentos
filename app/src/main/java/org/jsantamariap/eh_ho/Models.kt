package org.jsantamariap.eh_ho

import java.util.*

// un data class no hace salta los setters y getters
// nos ahorra mucho trabajo
data class Topic(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val date: Date = Date(),
    val posts: Int = 0,
    val views: Int = 0
) {
    // al colocar 1000L estamos diciendo que sea un Long
    // para ampliar el rango y que no halla un desbordamiento
    val MINUTE_MILLIS = 1000L * 60
    val HOUR_MILLIS = MINUTE_MILLIS * 60
    val DAY_MILLIS = HOUR_MILLIS * 24
    val MONTH_MILLIS = DAY_MILLIS * 30
    val YEAR_MILLIS = MONTH_MILLIS  * 12

    data class TimeOffSet(val amount: Int, val unit: Int)

    // se le pueden añadir funcionalidades extras que no
    // tengan que ver con los setters y getters
    fun getTimeOffSet(dateToCompare: Date = Date()): TimeOffSet {
        val currentDate = dateToCompare.time
        val difference = currentDate - this.date.time

        val years = difference / YEAR_MILLIS
        if (years > 0) return TimeOffSet(years.toInt(), Calendar.YEAR)

        val months = difference / MONTH_MILLIS
        if (months > 0) return TimeOffSet(months.toInt(), Calendar.MONTH)

        val days = difference /DAY_MILLIS
        if (days > 0) return TimeOffSet(days.toInt(), Calendar.DAY_OF_MONTH)

        val hours = difference / HOUR_MILLIS
        if (hours > 0) return TimeOffSet(hours.toInt(), Calendar.HOUR)

        val minutes = difference / MINUTE_MILLIS
        if (minutes > 0) return TimeOffSet(minutes.toInt(), Calendar.MINUTE)

        return TimeOffSet(0, Calendar.MINUTE)
    }
}