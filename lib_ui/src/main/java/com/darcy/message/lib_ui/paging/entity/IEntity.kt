package com.darcy.message.lib_ui.paging.entity

import java.time.LocalDateTime

/**
 * use sealed class to define entity
 */
sealed class IEntity() {
    /**
     * separator entity
     */
    data class SeparatorEntity(val id: Int, val title: String) : IEntity()

    /**
     * Article entity
     */
    data class Article(
        val id: Int,
        val title: String,
        val description: String,
        val created: LocalDateTime,
    ) : IEntity()
}