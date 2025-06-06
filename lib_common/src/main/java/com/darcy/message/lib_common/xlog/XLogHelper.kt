package com.darcy.message.lib_common.xlog

import android.content.Context
import com.darcy.message.lib_common.exts.FOR_TEST
import com.darcy.message.lib_common.exts.LOG_TAG
import com.darcy.message.lib_common.exts.print
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.ClassicFlattener
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy2
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator

object XLogHelper {
    fun forTest() {
        FOR_TEST = true
    }
    fun init(context: Context, isDebug: Boolean) {
//    initRelease(context)
        kotlin.runCatching {
            if (isDebug) {
                initDebug(context)
            } else {
                initRelease(context)
            }
        }.onFailure {
            it.print()
            it.printStackTrace()
        }
    }
    private fun initDebug(context: Context) {
        val level = LogLevel.ALL
        val config = LogConfiguration.Builder()
            .logLevel(level)
            .tag(LOG_TAG)
//            .enableThreadInfo()
//            .enableBorder()
//            .enableStackTrace(3)
            .build()
        val filePath = context.filesDir.absolutePath + "/xlog"
        val filePrinter: Printer = FilePrinter.Builder(filePath)
            .fileNameGenerator(DateFileNameGenerator())
            .backupStrategy(FileSizeBackupStrategy2(10 * 1024 * 1024, FileSizeBackupStrategy2.NO_LIMIT))
            .cleanStrategy(FileLastModifiedCleanStrategy(30 * 24 * 60 * 60 * 1000L))
            .flattener(ClassicFlattener())
            .build()
        val androidPrinter: Printer = AndroidPrinter()

        XLog.init(config, androidPrinter, filePrinter)
    }
    private fun initRelease(context: Context) {
        val level = LogLevel.NONE
        val config = LogConfiguration.Builder()
            .logLevel(level)
            .tag(LOG_TAG)
            .build()
        XLog.init(config)
    }
}