package com.darcy.message.sunflower.test

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.darcy.message.lib_db.db.DatabaseManager
import com.darcy.message.lib_db.tables.Item
import com.darcy.message.lib_ui.base.BaseActivity
import com.darcy.message.sunflower.R
import com.darcy.message.sunflower.databinding.AppActivityRoomTestBinding
import com.darcy.message.sunflower.test.adapter.RoomAdapter
import com.darcy.message.sunflower.test.viewmodel.RoomViewModel
import com.darcy.message.sunflower.ui.list.adapter.ListAdapter
import com.darcy.message.sunflower.ui.list.bean.ListBean
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class RoomTestActivity : BaseActivity<AppActivityRoomTestBinding>() {

    private var count = 0
    private val viewModel: RoomViewModel by viewModels()
    private val adapters = RoomAdapter()
    private val context: Context by lazy {
        this
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
    }

    private fun initObservers() {

    }

    override fun initView() {
        binding.recyclerView.apply {
//            adapter = listAdapter
            adapter = adapters
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            // add divider by itemDecoration
            val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
        }

        binding.run {
            btnTestItem.setOnClickListener {
                DatabaseManager.testDB(context)
            }
            btnAdd.setOnClickListener {
                viewModel.insertItem(Item(count, "Tom-$count", count.toDouble()))
                count++
            }
            btnDelete.setOnClickListener {
                val finalCount = count - 1
                viewModel.deleteItem(Item(finalCount, "Tom-$finalCount", finalCount.toDouble()))
            }
            btnDeleteAll.setOnClickListener {
                viewModel.deleteItemAll()
            }
            btnUpdate.setOnClickListener {
                viewModel.updateItem(1, "Tom-${System.currentTimeMillis()}")
            }
            btnQuery.setOnClickListener {
                lifecycleScope.launch() {
//                    queryItems()
                    queryRepos()
                }
            }

        }
    }

    override fun intListener() {

    }

    override fun initData() {

    }

    private suspend fun queryItems() {
        val items = viewModel.getItems()
        withContext(Dispatchers.Main) {
            adapters.setData(items?.map { ListBean().generate(it) } ?: listOf())
        }
    }

    private suspend fun queryRepos() {
        val items = viewModel.getRepos()
        withContext(Dispatchers.Main) {
            binding.tvInfo.text = items?.toString() ?: "Error"
        }
    }
}