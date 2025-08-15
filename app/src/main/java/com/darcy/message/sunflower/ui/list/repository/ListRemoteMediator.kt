package com.darcy.message.sunflower.ui.list.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_db.db.impl.RepoRoomDatabase
import com.darcy.message.lib_db.tables.RemoteKeys
import com.darcy.message.lib_db.tables.Repo
import com.darcy.message.sunflower.ui.list.api.FakeApi
import com.darcy.message.sunflower.ui.list.api.GithubService

private const val START_PAGE_INDEX: Int = 1

/**
 * mediator for local database data and remote http data
 * darcyRefactor: not well to be continued...
 */
@OptIn(ExperimentalPagingApi::class)
class ListRemoteMediator(
    private val query: String,
    private val fakeApi: FakeApi,
    private val gitApi: GithubService,
    private val database: RepoRoomDatabase
) : RemoteMediator<Int, Repo>() {
    override suspend fun initialize(): InitializeAction {
        // 初始化时 等待网络数据刷新本地数据
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
        // 初始化时 不刷新本地数据
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {
        try {
            // get page number
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    logV(message = "LoadType=Refresh")
                    val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKey?.nextKey?.minus(1) ?: START_PAGE_INDEX
                }

                LoadType.PREPEND -> {
                    logI(message = "LoadType=Prepend")
                    //return MediatorResult.Success(endOfPaginationReached = true)
                    val remoteKey = getRemoteKeyForFirstItem(state)
                    val preKey = remoteKey?.prevKey
                    if (preKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    }
                    preKey
                }

                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKey?.nextKey
                    logE(message = "LoadType=Append nextKey=$nextKey")
                    if (nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    }
                    nextKey
                }
            }
            logE(message = "page=$page")
//            val items = fakeApi.getRepos(page, state.config.pageSize)
            val items = gitApi.searchRepos(query, page, state.config.pageSize).items.also {
                logV(message = "items=${it}")
            }
            val endOfPaginationReached = items.isEmpty()
            database.withTransaction {
                // 清空数据
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.reposDao().clearRepos()
                }
                // 插入数据
                val prevKey = if (page == START_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = items.map {
                    RemoteKeys(itemId = it.id.toLong(), prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.reposDao().insertAll(items)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Repo>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                database.remoteKeysDao().remoteKeysRepoId(repo.id.toLong().also {
                    logV(message = "repoId=$it")
                })
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Repo>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data.also { logV(message = "repo list=$it") }
            ?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                database.remoteKeysDao().remoteKeysRepoId(repo.id.toLong().also {
                    logV(message = "repoId=$it")
                })
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Repo>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeysDao().remoteKeysRepoId(repoId.toLong())
            }
        }
    }

}
