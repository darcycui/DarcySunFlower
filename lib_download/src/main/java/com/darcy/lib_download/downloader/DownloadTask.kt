package com.darcy.lib_download.downloader

import com.darcy.lib_download.bean.ItemBean

data class DownloadTask(
    val itemBean: ItemBean,
    val listener: IDownloadListener?
) {
}