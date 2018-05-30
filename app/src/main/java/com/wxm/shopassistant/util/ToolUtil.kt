package com.wxm.shopassistant.util

import android.app.Activity

import java.lang.ref.WeakReference
import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * tool util
 * Created by WangXM on 2016/6/2.
 */
object ToolUtil {
    /**
     * in background-thread run [back]
     * after [back] and if no exception happen then run [ui] in ui-thread
     */
    fun runInBackground(h: Activity, back: () -> Unit, ui:  () -> Unit) {
        val weakActivity = WeakReference(h)
        var runRet = false
        Executors.newCachedThreadPool().submit {
            try {
                back()
                runRet = true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if(runRet) {
                weakActivity.get()?.let {
                    if (!(it.isDestroyed || it.isFinishing)) {
                        it.runOnUiThread(ui)
                    }
                }
            }
        }
    }

    /**
     * in background-thread run [back]
     * return callable result or [defRet] if exception happens
     * caller will wait result with timeout parameter [unit] and [timeout]
     */
    fun<T> callInBackground(back: ()->T, defRet: T, unit: TimeUnit, timeout: Long): T {
        val task = Executors.newCachedThreadPool().submit(back)
        try {
            return task.get(timeout, unit)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return defRet
    }
}
