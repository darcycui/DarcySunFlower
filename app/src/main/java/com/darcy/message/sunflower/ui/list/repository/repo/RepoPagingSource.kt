package com.darcy.message.sunflower.ui.list.repository.repo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_db.tables.Repo
import com.darcy.message.sunflower.ui.list.api.GithubService
import kotlinx.coroutines.CompletableDeferred

/**
 * repo 网络数据源
 */
class RepoPagingSource(
    private val query: String,
    private val repoRepository: RepoRepository = RepoRepository(GithubService.api())
) : PagingSource<Int, Repo>() {
    // 刷新键用于在初始加载后对 PagingSource.load 进行后续刷新调用
    override  fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
//        return null
        // 获取最接近最近访问的索引的页面的上一个键（如果 previous 为 null，则为 next 键）
        val refreshKey = state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
        logD("refreshKey: $refreshKey")
        return refreshKey
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val page = params.key ?: 1 // 页数
        val pageSize = params.loadSize // 每页数量
        // 异步结果
        val deferredResult: CompletableDeferred<LoadResult<Int, Repo>> = CompletableDeferred()
        // 请求网络数据
        repoRepository.getReposNew(
            query = query,
            page = page,
            itemsPerPage = pageSize,
            onSuccess = { result ->
                result?.let {
                    val repoItems = it.result.items
                    // 计算上一页 页数
                    val preKey = if (page > 1) page - 1 else null
                    // 计算下一页 页数
                    val nextKey = if (repoItems.isNotEmpty()) page + 1 else null
                    // 异步结束 返回 LoadResult.Page
                    deferredResult.complete(LoadResult.Page(repoItems, preKey, nextKey))
                }
            },
            onError = {
                // 异步结束
                deferredResult.complete(LoadResult.Error(Exception(it)))
            }
        )
        // 等待异步结果
        return deferredResult.await()
    }
}