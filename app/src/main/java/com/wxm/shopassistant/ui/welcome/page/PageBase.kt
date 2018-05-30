package com.wxm.shopassistant.ui.welcome.page

import android.app.Activity
import android.content.Intent
import com.wxm.shopassistant.define.GlobalDef
import com.wxm.shopassistant.util.let1

/**
 * @author      WangXM
 * @version     create：2018/5/28
 */
interface PageBase  {

    /**
     * if return true, means page can leave
     */
    fun leavePage(): Boolean

    fun doLogout(ac:Activity) {
        ac.let1 {
            it.setResult(GlobalDef.RET_USR_LOGOUT, Intent())
            it.finish()
        }
    }
}