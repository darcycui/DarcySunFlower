package com.darcy.message.lib_db.db

import com.darcy.message.lib_db.daos.ItemDao
import com.darcy.message.lib_db.daos.RemoteKeysDao
import com.darcy.message.lib_db.daos.RepoDao

interface IDatabase {
    fun show()
    fun itemDao(): ItemDao
    fun reposDao(): RepoDao
    fun remoteKeysDao(): RemoteKeysDao

}