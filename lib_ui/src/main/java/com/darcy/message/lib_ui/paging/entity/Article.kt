package com.darcy.message.lib_ui.paging.entity

import java.time.LocalDateTime

data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val created: LocalDateTime,
)