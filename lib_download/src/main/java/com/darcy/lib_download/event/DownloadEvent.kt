package com.darcy.lib_download.event

import android.os.Message


sealed class DownloadEvent(val what: Int) {
    companion object {

    }

    data object Start : DownloadEvent(1)
    data object Pause : DownloadEvent(2)
    data object Resume : DownloadEvent(3)
    data class ProgressUpdate(val progress: Double) : DownloadEvent(4)
    data object Cancel : DownloadEvent(5)
    data object FinishSuccess : DownloadEvent(6)
    data class FinishError(val e: Exception) : DownloadEvent(7)
    data object UnCompress : DownloadEvent(8)
    data object UnCompressSuccess : DownloadEvent(9)
    data object UnCompressError : DownloadEvent(10)
    data object Install : DownloadEvent(11)
    data object InstallSuccess : DownloadEvent(12)
    data object InstallError : DownloadEvent(13)
    data object Reset : DownloadEvent(100)

}

fun DownloadEvent.toMessage(): Message {
    return Message.obtain().also {
        it.what = this.what
        it.obj = this
    }
}