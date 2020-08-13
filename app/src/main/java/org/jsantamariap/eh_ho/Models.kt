package org.jsantamariap.eh_ho

import java.util.*

data class Topic(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String
)