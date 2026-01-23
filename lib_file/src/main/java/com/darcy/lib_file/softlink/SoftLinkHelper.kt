package com.darcy.lib_file.softlink

import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.exts.logE
import com.darcy.message.lib_common.exts.print
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


object SoftLinkHelper {
    fun softLink(src: String, dest: String) {
        val cmd = "ln -s $src $dest"
        logD("softLink cmd: $cmd")
        val process = Runtime.getRuntime().exec(cmd)
        process.waitFor()
    }

    fun checkSoftLink(path: String): Boolean {
        val cmd = "ls -l $path"
        logD("checkSoftLink cmd: $cmd")
        val process = Runtime.getRuntime().exec(cmd)
        val inputStream = process.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))
        val line = reader.readLine()
        logD("checkSoftLink result: $line")
        reader.close()
        return line?.startsWith("lrwx") ?: false
    }

    fun deleteSoftLink(path: String) {
        val cmd = "rm $path"
        val process = Runtime.getRuntime().exec(cmd)
        process.waitFor()
    }

    fun softLink2(src: String, dest: String) {
        val targetPath: Path? = Paths.get(src)
        val linkPath: Path? = Paths.get(dest)

        try {
            // 检查目标文件是否存在
            if (!Files.exists(targetPath)) {
                throw IOException("目标文件不存在: $src")
            }

            // 创建软链接
            Files.createSymbolicLink(linkPath, targetPath)
        } catch (e: IOException) {
            logE("软链接创建失败")
            e.print()
        }
    }

    fun checkSoftLink2(path: String): Boolean {
        val targetFile = File(path)
        targetFile.canonicalPath.also {
            logD("checkSoftLink2 canonicalPath: $it")
        }
        val targetPath: Path? = Paths.get(path)
        // 检查目标文件是否存在
        return Files.exists(targetPath)
    }
}