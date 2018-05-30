package com.wxm.shopassistant.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.wxm.shopassistant.data.item.LoginHistoryItem
import com.wxm.shopassistant.data.item.UsrItem
import com.wxm.shopassistant.define.GlobalDef
import com.wxm.shopassistant.util.AppUtil
import wxm.androidutil.log.TagLog
import java.sql.SQLException

/**
 * db ormlite helper
 * Created by WangXM on 2016/8/5.
 */
class DBHelper(context: Context)
    : OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    val usrItemREDao: RuntimeExceptionDao<UsrItem, Int> = getRuntimeExceptionDao(UsrItem::class.java)
    val loginHistoryREDao: RuntimeExceptionDao<LoginHistoryItem, Int> = getRuntimeExceptionDao(LoginHistoryItem::class.java)

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    override fun onCreate(db: SQLiteDatabase, connectionSource: ConnectionSource) {
        createAndInitTable()
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    override fun onUpgrade(db: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        try {
        } catch (e: SQLException) {
            TagLog.e("Can't upgrade databases", e)
            throw RuntimeException(e)
        }
    }

    private fun createAndInitTable() {
        try {
            TableUtils.createTable(connectionSource, UsrItem::class.java)
            TableUtils.createTable(connectionSource, LoginHistoryItem::class.java)
        } catch (e: SQLException) {
            TagLog.e("Can't create database", e)
            throw RuntimeException(e)
        }

        // 添加默认用户
        AppUtil.usrUtil.addUsr(GlobalDef.DEF_USR_NAME, GlobalDef.DEF_USR_PWD)
    }


    companion object {
        // dataBase file name
        private const val DATABASE_NAME = "AppLocal.db"

        // dataBase version
        private const val DATABASE_VERSION = 1
    }
}
