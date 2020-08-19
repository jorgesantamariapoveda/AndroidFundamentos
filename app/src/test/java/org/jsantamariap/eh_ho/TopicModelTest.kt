package org.jsantamariap.eh_ho

import org.jsantamariap.eh_ho.data.Topic
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class TopicModelTest {

    @Test
    fun getOffSet_Year_isCorrect() {
        val dateToCompare: Date = formatDate("01/01/2020 10:00:00")
        val testTopic = Topic(
            title = "Test",
            content = "Content tes",
            date = formatDate("01/01/2019 10:00:00")
        )

        val offset = testTopic.getTimeOffSet(dateToCompare)
        assertEquals("Amount comparision", 1, offset.amount)
        assertEquals("Unit comparison", Calendar.YEAR, offset.unit)
    }

    private fun formatDate(date: String): Date {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")

        // la forma antigua
        /*
        val dateFormatted: Date
        try {
            dateFormatted = formatter.parse(date)
        } catch (e: Exception) {
            throw e
        }

        return dateFormatted
         */

        // este operador ?: es de kotlin, no de java
        return formatter.parse(date)
            ?: throw IllegalArgumentException("Date ${date} has an incorrect format, try again with format dd/mm/yyyy hh:mm:ss")
    }
}