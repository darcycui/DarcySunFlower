package com.darcy.lib_download.ui.adapter

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darcy.lib_download.bean.ItemBean
import com.darcy.lib_download.databinding.LibDownloadItemBinding
import com.darcy.lib_download.downloader.DownloadManager
import com.darcy.lib_download.downloader.DownloadTask
import com.darcy.lib_download.downloader.IDownloadListener
import com.darcy.lib_download.event.DownloadEvent
import com.darcy.lib_download.event.toMessage
import com.darcy.lib_download.listener.IStateChangeListener
import com.darcy.lib_download.listener.IStateProgressChangeListener
import com.darcy.lib_download.state.DownloadingState
import com.darcy.lib_download.state.InitState
import com.darcy.lib_download.state.PauseState
import com.darcy.lib_download.statemachine.DownloadStateMachine
import com.darcy.lib_download.statemachine.IState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppAdapter : RecyclerView.Adapter<ViewHolder>() {
    private val dataList: MutableList<ItemBean> = mutableListOf()
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: LibDownloadItemBinding = LibDownloadItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, downloadListener)
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
            val stateMachine = DownloadStateMachine(stateChange, progressChange)
            item.stateMachine = stateMachine
            item
        })
        notifyDataSetChanged()
    }

    val stateChange = object : IStateChangeListener {
        override fun onStateChanged(
            task: DownloadTask,
            currentState: IState,
            message: Message?
        ) {
            val index = dataList.indexOf(task.itemBean)
            updateUI(index)
        }

        override fun onStatePreChange(
            task: DownloadTask,
            currentState: IState,
            message: Message?
        ) {
        }
    }
    val progressChange = object : IStateProgressChangeListener {
        override fun onProgressChange(
            task: DownloadTask,
            newState: IState,
            progress: Double
        ) {
            val index = dataList.indexOf(task.itemBean)
            updateUI(index)
        }
    }

    private fun updateUI(index: Int) {
        mainScope.launch {
            notifyItemChanged(index)
        }
    }

    val downloadListener = object : IDownloadListener {
        override fun onStart(task: DownloadTask) {
            val stateMachine = task.itemBean.stateMachine
            stateMachine?.sendMessage(DownloadEvent.Start.toMessage())
        }

        override fun onProgress(
            task: DownloadTask,
            progress: Double
        ) {
            val stateMachine = task.itemBean.stateMachine
            stateMachine?.sendMessage(DownloadEvent.ProgressUpdate(progress).toMessage())
        }

        override fun onPause(task: DownloadTask) {
            val stateMachine = task.itemBean.stateMachine
            stateMachine?.sendMessage(DownloadEvent.Pause.toMessage())
        }

        override fun onResume(task: DownloadTask) {
            val stateMachine = task.itemBean.stateMachine
            stateMachine?.sendMessage(DownloadEvent.Resume.toMessage())
        }

        override fun onCancel(task: DownloadTask) {
        }

        override fun onFinish(task: DownloadTask) {
            val stateMachine = task.itemBean.stateMachine
            stateMachine?.sendMessage(DownloadEvent.FinishSuccess.toMessage())
        }

        override fun onError(
            task: DownloadTask,
            e: Exception
        ) {
            val stateMachine = task.itemBean.stateMachine
            stateMachine?.sendMessage(DownloadEvent.FinishError(e).toMessage())
        }
    }
}

val scope = CoroutineScope(Dispatchers.IO)

class ViewHolder(
    private val binding: LibDownloadItemBinding,
    private val downloadListener: IDownloadListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindData(item: ItemBean) {
        binding.apply {
            itemTitle.text = item.name
            if (item.stateMachine?.currentState is DownloadingState) {
                val progress = (item.stateMachine?.currentState as DownloadingState).getProgress()
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
                        scope.launch {
                            val task = DownloadTask(item, downloadListener)
                            item.stateMachine?.setupDownloadTask(task)
                            DownloadManager.startDownload(task)
                        }
                    }
                }

                is DownloadingState -> {
                    itemActionButton.setOnClickListener {
                        item.stateMachine?.sendMessage(DownloadEvent.Pause.toMessage())
                    }
                }

                is PauseState -> {
                    itemActionButton.setOnClickListener {
                        item.stateMachine?.sendMessage(DownloadEvent.Resume.toMessage())
                    }
                }

                else -> {

                }
            }
        }
    }

}