package com.darcy.message.sunflower.test

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_data_store.helper.DataStoreEnum
import com.darcy.message.lib_data_store.helper.DataStoreHelper
import com.darcy.message.sunflower.R
import com.darcy.message.sunflower.databinding.ActivityDataStoreBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DataStoreActivity : AppCompatActivity() {
    private val binding: ActivityDataStoreBinding by lazy {
        ActivityDataStoreBinding.inflate(layoutInflater)
    }
    private val scope: CoroutineScope by lazy {
        MainScope()
    }
    private val context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initObservers()
    }

    private fun initObservers() {
        val keyName = "test"
        binding.btnSave.setOnClickListener {
            scope.launch {
                DataStoreHelper.getDataStore(context, DataStoreEnum.MAIN_DATA_STORE)?.let {
//                    DataStoreHelper.saveIntValue(dataStore = it, keyName = keyName, value = 1)
//                    DataStoreHelper.saveStringValue(dataStore = it, keyName = keyName, value = "data store string")
//                    DataStoreHelper.saveBooleanValue(dataStore = it, keyName = keyName, value = true)
//                    DataStoreHelper.saveFloatValue(dataStore = it, keyName = keyName, value = 3.14f)
                    DataStoreHelper.saveDoubleValue(dataStore = it, keyName = keyName, value = 9.80)
//                    DataStoreHelper.saveLongValue(dataStore = it, keyName = keyName, value = 1234L)
                }
            }
        }
        binding.btnGet.setOnClickListener {
            scope.launch {
                DataStoreHelper.getDataStore(context, DataStoreEnum.MAIN_DATA_STORE)?.let {
//                    val value = DataStoreHelper.getIntValue(dataStore = it, keyName = keyName)
//                    val value = DataStoreHelper.getStringValue(dataStore = it, keyName = keyName)
//                    val value = DataStoreHelper.getBooleanValue(dataStore = it, keyName = keyName)
//                    val value = DataStoreHelper.getFloatValue(dataStore = it, keyName = keyName)
                    val value = DataStoreHelper.getDoubleValue(dataStore = it, keyName = keyName)
//                    val value = DataStoreHelper.getLongValue(dataStore = it, keyName = keyName)
                    logD(message = "value=$value")
                    binding.tvInfo.text = value.toString()
                }
            }
        }
    }

    private fun initView() {

    }
}