package com.darcy.lib_download.actions

import com.darcy.lib_download.actions.downloader.IDownloadListener
import com.darcy.lib_download.actions.installer.IInstallListener
import com.darcy.lib_download.actions.unziper.IUnzipListener
import com.darcy.lib_download.bean.ItemBean

data class AppInstallTask(
    val itemBean: ItemBean,
    val listener: IDownloadListener?,
    val unzipListener: IUnzipListener?,
    val installListener: IInstallListener?
) {
}