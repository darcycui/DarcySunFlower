package com.darcy.lib_download.event

import android.os.Message


sealed class AppInstallEvent(val what: Int) {
    companion object {

    }

    data object StartDownload : AppInstallEvent(101)
    data object PauseDownload : AppInstallEvent(102)
    data object ResumeDownload : AppInstallEvent(103)
    data class UpdateProgressDownload(val progress: Double) : AppInstallEvent(104)
    data object CancelDownload : AppInstallEvent(105)
    data object FinishDownloadSuccess : AppInstallEvent(106)
    data class FinishDownloadError(val e: Exception) : AppInstallEvent(107)

    data object StartUnzip : AppInstallEvent(201)
    data object FinishUnzipSuccess : AppInstallEvent(202)
    data class FinishUnzipError(val e: Exception) : AppInstallEvent(203)
    data class UpdateProgressUnzip(val progress: Double) : AppInstallEvent(204)

    data object StartInstall : AppInstallEvent(301)
    data object FinishInstallSuccess : AppInstallEvent(302)
    data class FinishInstallError(val e: Exception) : AppInstallEvent(303)
    data class UpdateProgressInstall(val progress: Double) : AppInstallEvent(304)

    data object Reset : AppInstallEvent(999)

}

fun AppInstallEvent.toMessage(): Message {
    return Message.obtain().also {
        it.what = this.what
        it.obj = this
    }
}