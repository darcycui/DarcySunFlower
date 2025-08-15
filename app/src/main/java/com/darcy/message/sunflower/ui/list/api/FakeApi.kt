package com.darcy.message.sunflower.ui.list.api

import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_db.tables.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeApi @Inject constructor() {

    suspend fun getRepos(page: Int, pageSize: Int): List<Repo> {
        return withContext(Dispatchers.IO) {
            delay(2_000)
            logD(message = "getRepos: page=$page pageSize=$pageSize")
            mutableListOf<Repo>().apply {
                repeat(pageSize) {
                    val id = (page - 1) * pageSize + it + 1
                    logV(message = "getRepos: id=$id")
                    add(Repo(id, "name$id", "描述：$id", id + 1000))
                }
            }
        }
    }
}