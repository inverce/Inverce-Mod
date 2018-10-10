@file:JvmName("IO")

package com.inverce.mod.v2.core.utils

import java.io.*

fun Closeable?.closeSilently() {
    if (this != null) try {
        this.close()
    } catch (ignored: IOException) {
        /* ignored by user request */
    }
}

@Throws(IOException::class, FileNotFoundException::class)
fun File.copyFile(out: File) {
    FileInputStream(this).copyInto(FileOutputStream(out), true)
}

@Throws(IOException::class)
fun InputStream.copyInto(out: OutputStream, closeStreams: Boolean = false): Long {
    var total: Long = 0
    try {
        val buf = ByteArray(8096)
        while (true) {
            val r = this.read(buf)
            if (r == -1) {
                break
            }
            out.write(buf, 0, r)
            total += r.toLong()
        }
    } finally {
        if (closeStreams) {
            this.closeSilently()
            out.closeSilently()
        }
    }
    return total
}