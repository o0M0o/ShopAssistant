package com.wxm.shopassistant.data.db

import com.wxm.shopassistant.data.item.LoginHistoryItem
import com.wxm.shopassistant.data.item.UsrItem
import com.wxm.shopassistant.util.AppUtil
import java.sql.Timestamp

/**
 * use login history app can keep login status
 * @author      WangXM
 * @version     create：2018/5/28
 */
object LoginHistoryUtility {
    private fun getREDao() = AppUtil.dbHelper.loginHistoryREDao

    /**
     * add login history
     */
    fun addHistory(usr: UsrItem) {
        getREDao().create(LoginHistoryItem().apply {
            this.usr = usr
        })
    }

    /**
     * get last login after [ts]
     */
    fun getLastLoginAfter(ts: Timestamp): UsrItem? {
        val ls = getREDao().queryBuilder()
                .where().ge(LoginHistoryItem.FIELD_LOGIN_TIME, ts).let {
                    getREDao().query(it.prepare())
                } ?: return null

        if (ls.isEmpty())
            return null

        ls.sortBy { it.loginTime }
        return ls[0].usr
    }

    /**
     * clean login history
     */
    fun cleanHistory() {
        getREDao().queryForAll().forEach {
            getREDao().deleteById(it.id)
        }
    }
}