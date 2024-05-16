package com.darcy.message.sunflower.ui.list.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.darcy.message.lib_common.exts.logV
import com.darcy.message.lib_db.db.impl.ItemRoomDatabase
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.sunflower.ui.list.api.NewsApi
import com.darcy.message.lib_db.tables.RemoteKeys

private const val START_KEY: Int = 1

// darcyRefactor not well to be continued...
@OptIn(ExperimentalPagingApi::class)
class ListRemoteMediator constructor(
    private val newsApi: NewsApi,
    private val database: ItemRoomDatabase
) : RemoteMediator<Int, Item>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Item>): MediatorResult {
        try {
            // get page number
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    logV(message = "LoadType=Refresh")
//                    null
//                    val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
//                    remoteKey?.nextKey?.minus(1) ?: START_KEY
                    START_KEY
                }

                LoadType.PREPEND -> {
                    logV(message = "LoadType=Prepend")
                    return MediatorResult.Success(endOfPaginationReached = true)
//                    val remoteKey = getRemoteKeyForFirstItem(state)
//                    val preKey = remoteKey?.prevKey
//                    if (preKey == null) {
//                        return MediatorResult.Success(
//                            endOfPaginationReached = remoteKey != null
//                        )
//                    }
//                    preKey
                }

                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKey?.nextKey
                    logV(message = "LoadType=Append nextKey=$nextKey")
                    if (nextKey == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = remoteKey != null
                        )
                    }
                    nextKey
                }

                else -> {
                    logV(message = "LoadType=LoadTypeUnknown")
                    START_KEY
                }
            }
            val items = newsApi.getNews(page, state.config.pageSize).also {
                logV(message = "items=$it")
            }
            val endOfPaginationReached = items.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.itemDao().clearItems()
                }
                val prevKey = if (page == START_KEY) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = items.map {
                    RemoteKeys(itemId = it.id.toLong(), prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                logV(message = "items2=$items")
                database.itemDao().insertAll(items)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Item>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data.also { logV(message = "repo list=$it") }
            ?.let { list ->
                list[list.size - 1].let { repo ->
                    // Get the remote keys of the last item retrieved
                    database.remoteKeysDao().remoteKeysRepoId(repo.id.toLong().also {
                        logV(message = "repoId=$it")
                    })
                }
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Item>): RemoteKeys? {
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
        state: PagingState<Int, Item>
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
