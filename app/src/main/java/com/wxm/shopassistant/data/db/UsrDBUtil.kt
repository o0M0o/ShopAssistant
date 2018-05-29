package com.wxm.shopassistant.data.db

import com.j256.ormlite.dao.RuntimeExceptionDao
import com.wxm.shopassistant.data.event.DBChangeEvent
import com.wxm.shopassistant.data.item.UsrItem
import com.wxm.shopassistant.define.EDBChange
import com.wxm.shopassistant.define.GlobalDef
import com.wxm.shopassistant.util.AppUtil
import org.greenrobot.eventbus.EventBus
import wxm.androidutil.db.DBUtilityBase
import wxm.androidutil.util.MD5Util
import wxm.androidutil.util.doJudge
import wxm.androidutil.util.forObj

/**
 * @author      WangXM
 * @version     create：2018/5/29
 */
@Suppress("MemberVisibilityCanBePrivate")
class UsrDBUtil : DBUtilityBase<UsrItem, Int>() {
    override fun getDBHelper(): RuntimeExceptionDao<UsrItem, Int> = AppUtil.dbHelper.usrItemREDao

    override fun onDataCreate(p0: MutableList<Int>?) {
        EventBus.getDefault().post(DBChangeEvent(p0!!, EDBChange.CREATE))
    }

    override fun onDataRemove(p0: MutableList<Int>?) {
        EventBus.getDefault().post(DBChangeEvent(p0!!, EDBChange.DELETE))
    }

    override fun onDataModify(p0: MutableList<Int>?) {
        EventBus.getDefault().post(DBChangeEvent(p0!!, EDBChange.MODIFY))
    }

    /**
     * return true if usr with name [usr] is existed, else false
     */
    fun hasUsr(usr: String): Boolean {
        if (usr.isEmpty())
            return false

        return dbHelper.queryForEq(UsrItem.FIELD_NAME, usr).let {
            !(null == it || it.size < 1)
        }
    }


    /**
     * add usr with name [usr], password [pwd]
     * return usrItem for success or null for failure
     */
    fun addUsr(usr: String, pwd: String): UsrItem? {
        if (usr.isEmpty() || pwd.isEmpty())
            return null

        if(hasUsr(usr))
            return null

        return UsrItem().apply{
            this.name = usr
            this.pwd = pwd
        }.let {
            createData(it).doJudge(it, null)
        }
    }


    /**
     * return true if usr with name is [usr] and password is [pwd] exist,
     * else false
     */
    fun checkUsr(usr: String, pwd: String): Boolean {
        val query = dbHelper.queryBuilder().where()
                .eq(UsrItem.FIELD_NAME, usr)
                .and()
                .eq(UsrItem.FIELD_PWD, getMd5Pwd(pwd))
                .prepare()

        return dbHelper.query(query).let {
            null != it && it.isNotEmpty()
        }
    }


    /**
     *
     * return item if usr with name is [usr] and password is [pwd] exist,
     * else null
     */
    fun checkGetUsr(usr: String, pwd: String): UsrItem? {
        val query = dbHelper.queryBuilder().where()
                .eq(UsrItem.FIELD_NAME, usr)
                .and()
                .eq(UsrItem.FIELD_PWD, getMd5Pwd(pwd))
                .prepare()

        return dbHelper.query(query).let {
            (null == it || it.isEmpty()).doJudge(null, it[0])
        }
    }

    /**
     * use usr name [usr] and password [pwd] login app
     * and record in history
     *
     * return true if everything ok
     */
    fun loginByUsr(usr: String, pwd: String): Boolean {
        return checkGetUsr(usr, pwd).forObj(
                {loginByUsr(it, true)}, {false})
    }

    /**
     * use usr [usr] login app
     * if [recordHistory] is true, record login history
     *
     * return true if everything ok
     */
    fun loginByUsr(usr: UsrItem, recordHistory: Boolean): Boolean {
        AppUtil.curUsr = usr
        if (recordHistory) LoginHistoryUtility.addHistory(usr)
        return true
    }

    /**
     * return [orgPwd] as MD5 string
     */
    private fun getMd5Pwd(orgPwd: String): String {
        return (orgPwd.length < GlobalDef.STR_PWD_PAD.length).doJudge(
                { orgPwd + GlobalDef.STR_PWD_PAD.substring(orgPwd.length) },
                { orgPwd }
        ).let {
            MD5Util.string2MD5(it)
        }
    }
}