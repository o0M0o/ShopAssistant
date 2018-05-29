package com.wxm.shopassistant.util

import com.wxm.shopassistant.define.GlobalDef

/**
 * @author      WangXM
 * @version     create：2018/5/29
 */

/**
 * return full path use [dir] as directory path and [fn] as file name
 */
fun createPath(dir:String, fn:String): String   {
    return "$dir${GlobalDef.FILE_PATH_SEPARATOR}$fn"
}