package net.onefivefour.android.bitpot.data.meta

import java.io.File

object FileLoader {

    fun getJson(fileName: String): String {
        val uri = this.javaClass.classLoader?.getResource(fileName)
            ?: throw NullPointerException("cannot getJson. ClassLoader is null.")
        val file = File(uri.path)
        return String(file.readBytes())
    }
}