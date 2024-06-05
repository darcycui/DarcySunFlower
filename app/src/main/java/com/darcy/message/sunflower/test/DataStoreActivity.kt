package com.darcy.message.sunflower.test

import android.content.Context
import android.os.Bundle
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_data_store.helper.DataStoreEnum
import com.darcy.message.lib_data_store.helper.DataStoreHelper
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.sunflower.databinding.AppActivityDataStoreBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DataStoreActivity : BaseActivity<AppActivityDataStoreBinding>() {
    private val scope: CoroutineScope by lazy {
        MainScope()
    }
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        val keyName = "testKey"
        binding.btnSave.setOnClickListener {
            scope.launch {
                DataStoreHelper.getDataStore(context, DataStoreEnum.MAIN_DATA_STORE)?.let {
                    DataStoreHelper.run {
//                        saveIntValue(dataStore = it, keyName = keyName, value = 1)
//                        saveStringValue(
//                            dataStore = it,
//                            keyName = keyName,
//                            value = "data store string"
//                        )
//                        saveBooleanValue(dataStore = it, keyName = keyName, value = true)
//                        saveFloatValue(dataStore = it, keyName = keyName, value = 3.14f)
                        saveDoubleValue(dataStore = it, keyName = keyName, value = 9.80)
//                        saveLongValue(dataStore = it, keyName = keyName, value = 1234L)
                    }
                }
            }
        }
        binding.btnGet.setOnClickListener {
            scope.launch {
                DataStoreHelper.getDataStore(context, DataStoreEnum.MAIN_DATA_STORE)?.let {
                    DataStoreHelper.run {
//                    val value = getIntValue(dataStore = it, keyName = keyName)
//                    val value = getStringValue(dataStore = it, keyName = keyName)
//                    val value = getBooleanValue(dataStore = it, keyName = keyName)
//                    val value = getFloatValue(dataStore = it, keyName = keyName)
                        val value = getDoubleValue(dataStore = it, keyName = keyName)
//                    val value = getLongValue(dataStore = it, keyName = keyName)
                        logD(message = "value=$value")
                        binding.tvInfo.text = value.toString()
                    }
                }
            }
        }
    }

    override fun initView() {

    }

    override fun intListener() {

    }

    override fun initData() {

    }
}