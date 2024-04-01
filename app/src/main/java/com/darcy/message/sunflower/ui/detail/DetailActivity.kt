package com.darcy.message.sunflower.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.sunflower.R
import com.darcy.message.sunflower.databinding.ActivityDetailBinding
import com.darcy.message.sunflower.databinding.ActivityListBinding
import com.darcy.message.sunflower.ui.detail.viewmodel.DetailViewModel
import com.darcy.message.sunflower.ui.list.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @AndroidEntryPoint
 * hilt inject Activity
 */
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private val context: Context by lazy {
        this
    }
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    // inject ViewModel
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_detail)
        setContentView(binding.root)
        initView()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        binding.btnUpdate.setOnClickListener {
            lifecycleScope.launch {
                viewModel.updateItem(1, "Tom-${System.currentTimeMillis()}")
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            // use flow to trigger UI update when data changed
            viewModel.getItemDetail(1).asLiveData().observe(this@DetailActivity) {
                it?.let {
                    logD(message = "item-->$it")
                    binding.tvInfo.text = it.itemName
                } ?: run {
                    logE(message = "item is null")
                }
            }
        }
    }

    private fun initView() {

    }
}