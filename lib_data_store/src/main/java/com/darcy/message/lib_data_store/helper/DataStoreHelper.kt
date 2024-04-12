package com.darcy.message.lib_data_store.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Description:DataStore util class used to save key-value pairs recommended by google
 * This class has already initialized a default DataStore named "mainDataStore",most of the time we can use this DataStore directly.
 * If you want to add a new DataStore,you can follow the steps below
 * 1.Create a new DataStoreEnum in [DataStoreEnum]
 * 2.Add a new DataStore field in [DataStoreHelper], you should use the extension function [preferencesDataStore]
 * 3.Add the new DataStore to cache map [dataStoreCachedMap] in [init] method
 * @author: Darcy
 * @date: 2022/09/01 16:02
 */
object DataStoreHelper {

    private val dataStoreCachedMap: MutableMap<DataStoreEnum, DataStore<Preferences>> = mutableMapOf()

    private val Context.mainDataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreEnum.MAIN_DATA_STORE.getName())
    fun init(context: Context) {
        dataStoreCachedMap[DataStoreEnum.MAIN_DATA_STORE] = context.mainDataStore
    }

    fun getDataStore(context: Context, name: DataStoreEnum): DataStore<Preferences>? {
        return if (dataStoreCachedMap.containsKey(name)) {
            dataStoreCachedMap[name]
        } else {
            logE(message = "DataStoreHelper getDataStore name:$name not found")
            null
        }
    }

    suspend fun saveIntValue(dataStore: DataStore<Preferences>, keyName: String, value: Int) {
        logD(message = "saveIntValue keyName:$keyName,value:$value")
        val key = intPreferencesKey(keyName)
        dataStore.edit {
            it[key] = value
        }
    }

    suspend fun getIntValue(
        dataStore: DataStore<Preferences>,
        keyName: String,
        defaultValue: Int? = null
    ): Int {
        logD(message = "getIntValue keyName:$keyName,defaultValue:$defaultValue")
        val key = intPreferencesKey(keyName)
        return dataStore.data.map {
            it[key] ?: (defaultValue ?: 0)
        }.first().also {
            logD(message = "getIntValue result:$it")
        }
    }

    suspend fun saveStringValue(dataStore: DataStore<Preferences>, keyName: String, value: String) {
        logD(message = "saveStringValue keyName:$keyName,value:$value")
        val key = stringPreferencesKey(keyName)
        dataStore.edit {
            it[key] = value
        }
    }

    suspend fun getStringValue(
        dataStore: DataStore<Preferences>,
        keyName: String,
        defaultValue: String? = null
    ): String {
        logD(message = "getStringValue keyName:$keyName,defaultValue:$defaultValue")
        val key = stringPreferencesKey(keyName)
        return dataStore.data.map {
            it[key] ?: (defaultValue ?: "")
        }.first().also {
            logD(message = "getStringValue result:$it")
        }
    }

    suspend fun saveBooleanValue(dataStore: DataStore<Preferences>, keyName: String, value: Boolean) {
        logD(message = "saveBooleanValue keyName:$keyName,value:$value")
        val key = booleanPreferencesKey(keyName)
        dataStore.edit {
            it[key] = value
        }
    }

    suspend fun getBooleanValue(
        dataStore: DataStore<Preferences>,
        keyName: String,
        defaultValue: Boolean? = null
    ): Boolean {
        logD(message = "getBooleanValue keyName:$keyName,defaultValue:$defaultValue")
        val key = booleanPreferencesKey(keyName)
        return dataStore.data.map {
            it[key] ?: (defaultValue ?: false)
        }.first().also {
            logD(message = "getBooleanValue result:$it")
        }
    }

    suspend fun saveLongValue(dataStore: DataStore<Preferences>, keyName: String, value: Long) {
        logD(message = "saveLongValue keyName:$keyName,value:$value")
        val key = longPreferencesKey(keyName)
        dataStore.edit {
            it[key] = value
        }
    }

    suspend fun getLongValue(dataStore: DataStore<Preferences>, keyName: String, defaultValue: Long? = null): Long {
        logD(message = "getLongValue keyName:$keyName,defaultValue:$defaultValue")
        val key = longPreferencesKey(keyName)
        return dataStore.data.map {
            it[key] ?: (defaultValue ?: 0L)
        }.first().also {
            logD(message = "getLongValue result:$it")
        }
    }

    suspend fun saveFloatValue(dataStore: DataStore<Preferences>, keyName: String, value: Float) {
        logD(message = "saveFloatValue keyName:$keyName,value:$value")
        val key = floatPreferencesKey(keyName)
        dataStore.edit {
            it[key] = value
        }
    }

    suspend fun getFloatValue(dataStore: DataStore<Preferences>, keyName: String, defaultValue: Float? = null): Float {
        logD(message = "getFloatValue keyName:$keyName,defaultValue:$defaultValue")
        val key = floatPreferencesKey(keyName)
        return dataStore.data.map {
            it[key] ?: (defaultValue ?: 0f)
        }.first().also {
            logD(message = "getFloatValue result:$it")
        }
    }

    suspend fun saveDoubleValue(dataStore: DataStore<Preferences>, keyName: String, value: Double) {
        logD(message = "saveDoubleValue keyName:$keyName,value:$value")
        val key = doublePreferencesKey(keyName)
        dataStore.edit {
            it[key] = value
        }
    }

    suspend fun getDoubleValue(dataStore: DataStore<Preferences>, keyName: String, defaultValue: Double? = null): Double {
        logD(message = "getDoubleValue keyName:$keyName,defaultValue:$defaultValue")
        val key = doublePreferencesKey(keyName)
        return dataStore.data.map {
            it[key] ?: (defaultValue ?: 0.0)
        }.first().also {
            logD(message = "getDoubleValue result:$it")
        }
    }

}