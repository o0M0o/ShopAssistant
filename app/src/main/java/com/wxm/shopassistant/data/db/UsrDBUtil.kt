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

/**
 * @author      WangXM
 * @version     create：2018/5/29
 */
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

        val ui = UsrItem()
        ui.name = usr
        ui.pwd = pwd

        if (createData(ui)) {

        } else  {
            return null
        }

        val md5Pwd = getMd5Pwd(pwd)
        var uiret: UsrItem? = null
        val ret = dbHelper.queryForEq(UsrItem.FIELD_NAME, usr)
        if (null == ret || ret.size < 1) {

        } else {
            val uiold = ret[0]
            if (pwdpad != uiold.pwd) {
                uiold.pwd = pwdpad
                if (modifyData(uiold))
                    uiret = uiold
            } else {
                uiret = uiold
            }
        }

        return uiret
    }


    /**
     * 检查用户
     * @param usr 待检查用户名
     * @param pwd 待检查用户密码
     * @return 如果符合返回true, 否则返回false
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
     * 检查登录信息，如果有符合的记录就返回对应用户信息
     * @param usr 待检查用户名
     * @param pwd 待检查用户密码
     * @return 如果符合返回注册用户数据, 否则返回null
     */
    fun checkAndGetUsr(usr: String, pwd: String): UsrItem? {
        var pwdpad = pwd
        if (pwdpad.length < GlobalDef.STR_PWD_PAD.length) {
            pwdpad += GlobalDef.STR_PWD_PAD.substring(pwd.length)
        }

        val lsui = dbHelper.queryForEq(UsrItem.FIELD_NAME, usr)
        if (null == lsui || lsui.size < 1)
            return null

        val checkPwd = MD5Util.string2MD5(pwdpad)
        return if (checkPwd == lsui[0].pwd) lsui[0] else null

    }

    /**
     * 使用用户信息登录APP
     * （并刷新数据)
     * @param usr 待检查用户名
     * @param pwd 待检查用户密码
     * @return 如果登录成功返回true, 否则返回false
     */
    fun loginByUsr(usr: String, pwd: String): Boolean {
        val ui = CheckAndGetUsr(usr, pwd) ?: return false
        return loginByUsr(ui, true)
    }

    fun loginByUsr(usr: UsrItem, recordHistory: Boolean): Boolean {
        AppUtil.curUsr = usr
        if (recordHistory) LoginHistoryUtility.addHistory(usr)
        return true
    }

    private fun getMd5Pwd(orgPwd: String): String {
        return (orgPwd.length < GlobalDef.STR_PWD_PAD.length).doJudge(
                { orgPwd + GlobalDef.STR_PWD_PAD.substring(orgPwd.length) },
                { orgPwd }
        ).let {
            MD5Util.string2MD5(it)
        }
    }
}