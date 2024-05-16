package com.darcy.message.sunflower.ui.list.api

import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_db.tables.Item
import kotlinx.coroutines.delay
import javax.inject.Inject

class NewsApi @Inject constructor() {
    suspend fun getNews(page: Int, pageCount: Int): List<Item> {
        delay(2_000)
        logV("NewsApi", message = "getNews: page=$page pageCount=$pageCount")
        return mutableListOf<Item>().also { list ->
            repeat(pageCount) {
                val id = (page - 1) * pageCount + it
                logI("NewsApi", message = "getNews: id=$id")
                list.add(Item(id, "title$id", it + 3.14, 100))
            }
        }
    }
}