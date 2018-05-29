package com.wxm.shopassistant.util

import com.wxm.shopassistant.data.db.DBHelper
import com.wxm.shopassistant.data.db.UsrDBUtil
import wxm.KeepAccount.R
import wxm.KeepAccount.db.*
import wxm.KeepAccount.define.IncomeNoteItem
import wxm.KeepAccount.item.BudgetItem
import wxm.KeepAccount.item.PayNoteItem
import wxm.KeepAccount.item.RemindItem
import wxm.KeepAccount.item.UsrItem
import wxm.KeepAccount.ui.utility.NoteDataHelper
import wxm.androidutil.app.AppBase
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

    }

    companion object {
        private const val USR_DEFAULT_ICON_NAME = "usr_default_icon.png"

        val self: AppUtil
            get() = (appContext() as AppUtil)

        val imageDirPath: String
            get() = self.mImageDirPath

        val usrDefaultIconPath: String
            get() = createPath(self.mImageDirPath, USR_DEFAULT_ICON_NAME)

        /**
         * DB helper
         */
        val dbHelper: DBHelper
            get() = self.mDBHelper

        val usrDBUtil :UsrDBUtil
            get() = self.mUsrDBUtil

        /**
         * get current usr
         * @return      current usr
         */
        /**
         * set current usr
         * @param cur_usr   current usr
         */
        var curUsr: UsrItem?
            get() = self.mUICurUsr
            set(cur_usr) {
                self.mUICurUsr = cur_usr
            }
    }
}
