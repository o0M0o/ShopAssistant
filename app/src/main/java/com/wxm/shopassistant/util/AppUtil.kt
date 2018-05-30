package com.wxm.shopassistant.util

import android.graphics.BitmapFactory
import com.wxm.shopassistant.R
import com.wxm.shopassistant.data.db.DBHelper
import com.wxm.shopassistant.data.db.UsrDBUtil
import com.wxm.shopassistant.data.item.UsrItem
import com.wxm.shopassistant.define.GlobalDef
import wxm.androidutil.app.AppBase
import wxm.androidutil.image.ImageUtil
import wxm.androidutil.util.FileUtil
import java.io.File
import java.nio.file.Files

/**
 * app leave util
 * Created by WangXM on 2016/5/7.
 */
class AppUtil : AppBase() {
    // for sqlite
    private lateinit var mDBHelper: DBHelper
    private lateinit var mUsrDBUtil: UsrDBUtil

    // for dir
    private lateinit var mImageDirPath: String

    // for usr
    private var mUICurUsr: UsrItem? = null

    override fun onCreate() {
        // TODO Auto-generated method stub
        super.onCreate()
        initDB()
        initDirectory()
    }

    override fun onTerminate() {
        super.onTerminate()
        closeDB()
    }

    private fun initDB()    {
        mDBHelper = DBHelper(appContext())
        mUsrDBUtil = UsrDBUtil()
    }

    private fun closeDB()   {
        mDBHelper.close()
    }

    private fun initDirectory() {
        val rootDir = filesDir
        val imagePath = "$rootDir/image"
        File(imagePath).let {
            if (!it.exists()) {
                it.mkdirs()
            } else true
        }.let {
            mImageDirPath = if (it) imagePath else rootDir.path
        }

        File(AppUtil.usrDefaultIconPath).let1 {
            if (!it.exists()) {
                BitmapFactory.decodeResource(resources, R.drawable.ic_usr_big).let1 {
                    ImageUtil.saveBitmapToJPGFile(it, AppUtil.imageDirPath, GlobalDef.USR_DEFAULT_ICON_NAME)
                }
            }
        }
    }

    companion object {
        val self: AppUtil
            get() = (appContext() as AppUtil)

        val imageDirPath: String
            get() = self.mImageDirPath

        val usrDefaultIconPath: String
            get() = createPath(self.mImageDirPath, GlobalDef.USR_DEFAULT_ICON_NAME)

        /**
         * DB helper
         */
        val dbHelper: DBHelper
            get() = self.mDBHelper

        /**
         * for usr
         */
        val usrUtil :UsrDBUtil
            get() = self.mUsrDBUtil

        /**
         * get or set current usr
         */
        var curUsr: UsrItem?
            get() = self.mUICurUsr
            set(cur_usr) {
                self.mUICurUsr = cur_usr
            }
    }
}
