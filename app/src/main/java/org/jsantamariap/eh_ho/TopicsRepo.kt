package org.jsantamariap.eh_ho

// object se usa para patrón de diseño Singleton
object TopicsRepo {
    val topics: MutableList<Topic> = mutableListOf()
        // get custom, justamente debajo de la propiedad (por el uso de field)
        // si se usa directamente topics se produce un stackoverflow, recursiva
        get() {
            if (field.isEmpty())
                field.addAll(createDummyTopics())
            return field
        }


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
                title = "Topic $it",
                content = "Content $it"
            )
        }

    // otro uso de una funcion inline
    fun getTopic(id: String): Topic? = topics.find {
        it.id == id
    }
}