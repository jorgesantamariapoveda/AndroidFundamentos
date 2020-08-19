package org.jsantamariap.eh_ho

import org.jsantamariap.eh_ho.data.SignUpModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class SignUpModelTest {

    @Test
    fun toJson_isCorrect() {
        val model = SignUpModel(
            "test",
            "test@test.com",
            "12345678"
        )

        // la primera vez que lo ejecute fallará porque falta
        // el import org.json.JSONObject cuando se pasan los test
        // para arreglarlo hay que ir a build.gradle y añadirlo
        // a la lista de dependencias como
        // testImplementation 'org.json:json:20190722'
        val json = model.toJson()

        assertEquals("test", json.get("name"))
        assertEquals("test", json.get("username"))
        assertEquals("test@test.com", json.get("email"))
        assertEquals("12345678", json.get("password"))
        assertEquals(true, json.get("active"))
        assertEquals(true, json.get("approved"))
    }

    @Test
    fun toJson_isFailure() {
        val model = SignUpModel(
            "test",
            "test@test.com",
            "12345678"
        )

        // la primera vez que lo ejecute fallará porque falta
        // el import org.json.JSONObject cuando se pasan los test
        // para arreglarlo hay que ir a build.gradle y añadirlo
        // a la lista de dependencias como
        // testImplementation 'org.json:json:20190722'
        val json = model.toJson()

        assertNotEquals("tes", json.get("name"))
        assertNotEquals("tes", json.get("username"))
        assertNotEquals("tes@test.com", json.get("email"))
        assertNotEquals("1234567", json.get("password"))
        assertNotEquals(false, json.get("active"))
        assertNotEquals(false, json.get("approved"))
    }
}