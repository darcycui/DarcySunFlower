package com.darcy.message.sunflower.ui.list.api

import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.lib_db.tables.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TestApi @Inject constructor() {
    suspend fun getNews(page: Int, pageCount: Int): List<Item> {
        return withContext(Dispatchers.IO) {
            delay(2_000)
            logV("NewsApi", message = "getNews: page=$page pageCount=$pageCount")
            mutableListOf<Item>().also { list ->
                repeat(pageCount) {
                    val id = (page - 1) * pageCount + it
                    logI("NewsApi", message = "getNews: id=$id")
                    list.add(Item(id, "title$id", it + 3.14, 100))
                }
            }
        }
    }

    suspend fun getRepos(page: Int, pageSize: Int): List<Repo> {
        return withContext(Dispatchers.IO) {
            delay(2_000)
            logD("NewsApi", message = "getRepos: page=$page pageSize=$pageSize")
            mutableListOf<Repo>().apply {
                repeat(pageSize) {
                    val id = (page - 1) * pageSize + it + 1
                    logV("NewsApi", message = "getRepos: id=$id")
                    add(
                        Repo(
                            id.toLong(),
                            "name$id",
                            "fullName$id",
                            "description$id",
                            "url$id",
                            1000 - id,
                            9000 - id,
                            "language$id"
                        )
                    )
                }
            }
        }
    }
}