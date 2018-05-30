package com.wxm.shopassistant.ui.welcome


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import com.wxm.shopassistant.R
import wxm.androidutil.ui.activity.ACSwitcherActivity
import com.wxm.shopassistant.define.GlobalDef


/**
 * first page after login
 */
class ACWelcome : ACSwitcherActivity<FrgWelcome>()  {

    override fun setupFragment(savedInstanceState: Bundle?) {
        addFragment(FrgWelcome())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig)
        hotFragment.reInitUI()
    }

    override fun leaveActivity() {
        if ((hotFragment as FrgWelcome).leaveFrg()) {
            setResult(GlobalDef.RET_USR_LOGOUT, Intent())
            finish()
        }
    }
}
