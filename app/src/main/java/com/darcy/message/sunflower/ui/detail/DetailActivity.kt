package com.darcy.message.sunflower.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.sunflower.databinding.AppActivityDetailBinding
import com.darcy.message.sunflower.ui.detail.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @AndroidEntryPoint
 * hilt inject Activity
 */
@AndroidEntryPoint
class DetailActivity : BaseActivity<AppActivityDetailBinding>() {
    private val context: Context by lazy {
        this
    }

    // inject ViewModel
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initListener() {
        initObservers()

        binding.btnUpdate.setOnClickListener {
            lifecycleScope.launch {
                viewModel.updateItem(1, "Tom-${System.currentTimeMillis()}")
            }
        }
        binding.btnTransaction.setOnClickListener {
            lifecycleScope.launch {
                viewModel.updateByDBTransaction()
            }
        }
    }

    private fun initObservers() {
//        lifecycleScope.launch {
//            // use flow to trigger UI update when data changed
//            viewModel.getItemDetailFlow(1).asLiveData().observe(this@DetailActivity) {
//                it?.let {
//                    logD(message = "item-->$it")
//                    binding.tvInfo.text = it.itemName
//                } ?: run {
//                    logE(message = "item is null")
//                }
//            }
//        }
        lifecycleScope.launch {
            // use flow to trigger UI update when data changed
            viewModel.getItemDetailLiveData(1).observe(this@DetailActivity) {
                it?.let {
                    logD(message = "item-->$it")
                    binding.tvInfo.text = it.itemName
                } ?: run {
                    logE(message = "item is null")
                }
            }
        }
    }

    override fun initView() {

    }

    override fun initData() {
    }
}