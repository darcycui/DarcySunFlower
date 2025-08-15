package com.darcy.message.sunflower.ui.list.repository.repo

import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.print
import com.darcy.message.lib_http.HttpManager
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.sunflower.ui.list.api.GithubService
import com.darcy.message.sunflower.ui.list.api.RepoSearchResponse
import kotlinx.serialization.serializer
import javax.inject.Inject

class RepoRepository @Inject constructor(private val api: GithubService) {
    suspend fun getRepos(
        query: String,
        page: Int,
        itemsPerPage: Int
    ): RepoSearchResponse? {
        return try {
            api.searchRepos(query, page, itemsPerPage)
        } catch (e: Exception) {
            logE("发生异常：${e.message}")
            e.print()
            null
        }
    }

    suspend fun getReposNew(
        query: String,
        page: Int,
        itemsPerPage: Int,
        onSuccess: (BaseResult<RepoSearchResponse>?) -> Unit,
        onError: (String) -> Unit
    ) {
        HttpManager.doGet(
            RepoSearchResponse::class.java,
            serializer<RepoSearchResponse>(),
            "",
            "",
            mapOf(),
            false
        ) {
            start {
                println("start")
            }
            request {
                println("request:")
                val items = api.searchRepos(query, page, itemsPerPage)
                val baseResult: BaseResult<RepoSearchResponse> = BaseResult(
                    200, -1, "search repos success", items
                )
                baseResult
//                    null
            }
            success {
                println("success:$it")
                onSuccess(it)
            }
            error {
                println("error:$it")
                onError(it)
            }
            finish {
                println("finish")
            }
        }
    }
}