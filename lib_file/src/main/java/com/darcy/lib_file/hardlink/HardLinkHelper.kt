package com.darcy.lib_file.hardlink

import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.logI
import com.darcy.message.lib_common.exts.print
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


object HardLinkHelper {
    fun hardLink(src: String, dest: String) {
        val cmd = "ln $src $dest"
        logD("-->hardLink cmd: $cmd")
        val process = Runtime.getRuntime().exec(cmd)
        process.waitFor()
    }

    fun checkHardLink(src: String): Boolean {
        val cmd = "ls -l $src"
        logD("-->checkHardLink cmd: $cmd")
        val process = Runtime.getRuntime().exec(cmd)
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val line = reader.readLine()
        logD("-->checkHardLink result: $line")
        return line?.contains("->") ?: false
    }

    fun hardLink2(src: String, dest: String) {
        val sourcePath: Path = Paths.get(src)
        val linkPath: Path? = Paths.get(dest)

        try {
            // 确保源文件存在
            if (!sourcePath.toFile().exists()) {
                Files.createFile(sourcePath)
            }
            // 创建硬链接
            Files.createLink(linkPath, sourcePath)
            logI("硬链接创建成功: $linkPath")
        } catch (e: IOException) {
            logE("硬链接创建失败")
            e.print()
        }
    }

    fun checkHardLink2(src: String, dest: String): Boolean {
        val sourceFile = File(src)
        val linkFile = File(dest)
        return linkFile.exists() && linkFile.isFile && linkFile.canonicalPath == sourceFile.canonicalPath
    }
}