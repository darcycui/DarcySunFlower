package com.darcy.lib_download.ui.adapter

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darcy.lib_download.actions.AppInstallTask
import com.darcy.lib_download.actions.downloader.IDownloadListener
import com.darcy.lib_download.actions.installer.IInstallListener
import com.darcy.lib_download.actions.unziper.IUnzipListener
import com.darcy.lib_download.bean.ItemBean
import com.darcy.lib_download.databinding.LibDownloadItemBinding
import com.darcy.lib_download.event.AppInstallEvent
import com.darcy.lib_download.event.toMessage
import com.darcy.lib_download.listener.IStateChangeListener
import com.darcy.lib_download.listener.IStateProgressChangeListener
import com.darcy.lib_download.state.DownloadPauseState
import com.darcy.lib_download.state.DownloadingState
import com.darcy.lib_download.state.InitState
import com.darcy.lib_download.statemachine.AppInstallStateMachine
import com.darcy.lib_download.statemachine.IState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppAdapter : RecyclerView.Adapter<ViewHolder>() {
    private val dataList: MutableList<ItemBean> = mutableListOf()
    private val mainScope = CoroutineScope(Dispatchers.Main)

    // 下载监听
    private val downloadListener = DownloadListenerImpl()

    // 解压监听
    private val unzipListener = UnzipListenerImpl()

    // 安装监听
    private val installListener = InstallListenerImpl()

    // 状态改变监听
    private val stateChangeListener = object : IStateChangeListener {
        override fun onStateChanged(
            task: AppInstallTask,
            currentState: IState,
            message: Message?
        ) {
            val index = dataList.indexOf(task.itemBean)
            // 更新UI
            updateUI(index)
        }

        override fun onStatePreChange(
            task: AppInstallTask,
            currentState: IState,
            message: Message?
        ) {
        }
    }

    // 进度改变监听
    private val progressChangeListener = object : IStateProgressChangeListener {
        override fun onProgressChange(
            task: AppInstallTask,
            newState: IState,
            progress: Double
        ) {
            val index = dataList.indexOf(task.itemBean)
            // 更新UI
            updateUI(index)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: LibDownloadItemBinding = LibDownloadItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, downloadListener, unzipListener, installListener)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bindData(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(dataList: List<ItemBean>) {
        this.dataList.clear()
        this.dataList.addAll(dataList.map { item ->
            val stateMachine = AppInstallStateMachine(stateChangeListener, progressChangeListener)
            item.stateMachine = stateMachine
            item
        })
        notifyDataSetChanged()
    }

    /**
     * 状态变化后触发 更新UI
     */
    private fun updateUI(index: Int) {
        mainScope.launch {
            notifyItemChanged(index)
        }
    }
}

class ViewHolder(
    private val binding: LibDownloadItemBinding,
    private val downloadListener: IDownloadListener,
    private val unzipListener: IUnzipListener,
    private val installListener: IInstallListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindData(item: ItemBean) {
        binding.apply {
            itemTitle.text = item.name
            if (item.stateMachine?.currentState is DownloadingState) {
                val progress =
                    (item.stateMachine?.currentState as DownloadingState).getProgress()
                itemProgress.progress = (progress * 100).toInt()
                itemActionButton.visibility = View.GONE
                itemProgress.visibility = View.VISIBLE
            } else {
                itemActionButton.visibility = View.VISIBLE
                itemProgress.visibility = View.GONE
            }
            val state = item.stateMachine?.currentState
            itemActionButton.text = state?.name ?: "未知"
            when (state) {
                is InitState -> {
                    itemActionButton.setOnClickListener {
                        val task =
                            AppInstallTask(item, downloadListener, unzipListener, installListener)
                        item.stateMachine?.setupDownloadTask(task)
                        item.stateMachine?.sendMessage(AppInstallEvent.StartDownload.toMessage())
                    }
                }

                is DownloadingState -> {
                    itemActionButton.setOnClickListener {
                        item.stateMachine?.sendMessage(AppInstallEvent.PauseDownload.toMessage())
                    }
                }

                is DownloadPauseState -> {
                    itemActionButton.setOnClickListener {
                        item.stateMachine?.sendMessage(AppInstallEvent.ResumeDownload.toMessage())
                    }
                }

                else -> {

                }
            }
        }
    }

}