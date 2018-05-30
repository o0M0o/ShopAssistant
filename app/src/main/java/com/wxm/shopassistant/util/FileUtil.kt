package com.wxm.shopassistant.util

import android.net.Uri
import android.provider.MediaStore
import com.wxm.shopassistant.define.GlobalDef
import wxm.androidutil.log.TagLog
import wxm.androidutil.util.forObj
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * @author      WangXM
 * @version     create：2018/5/29
 */

private fun getRealPathFromURI(contentURI: Uri): String {
    return AppUtil.self.contentResolver
            .query(contentURI, null, null, null, null)
            .forObj(
                    {
                        it.use {
                            it.moveToFirst()
                            val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                            it.getString(idx)

                        }
                    },
                    { contentURI.path }
            )
}


/**
 * return full path use [dir] as directory path and [fn] as file name
 */
fun createPath(dir:String, fn:String): String   {
    return "$dir${GlobalDef.FILE_PATH_SEPARATOR}$fn"
}

/**
 * get file suffixes for [fn]
 * example : "123/456/abc.jpg" -> ".jpg"
 */
fun getFileSuffixes(fn:String): String  {
    return fn.lastIndexOf(".").let {
        if(-1 != it)    fn.substring(it)
        else ""
    }
}

/**
 * save image from [imageUri] to app image directory
 * return image file name when success else ""
 */
fun saveImage(imageUri: Uri): String {
    val realPath = getRealPathFromURI(imageUri)
    val newFN = createPath(AppUtil.imageDirPath,   UUID.randomUUID().toString() + getFileSuffixes(realPath))
    try {
        fileCopy(File(realPath), File(newFN))
    } catch (e:IOException) {
        TagLog.e("", e)
        return ""
    }

    return newFN
}

/**
 * copy file from [src] to [dst]
 */
@Throws(IOException::class)
private fun fileCopy(src: File, dst: File) {
    FileInputStream(src).use { inStream ->
        val inChannel = inStream.channel
        FileOutputStream(dst).use { outStream ->
            val outChannel = outStream.channel
            inChannel.transferTo(0, inChannel.size(), outChannel)
        }

        Unit
    }
}

