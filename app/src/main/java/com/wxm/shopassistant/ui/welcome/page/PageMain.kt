package com.wxm.shopassistant.ui.welcome.page

import android.os.Bundle
import com.wxm.shopassistant.R
import wxm.androidutil.ui.frg.FrgSupportBaseAdv

/**
 * for welcome
 * Created by WangXM on 2016/12/7.
 */
class PageMain : FrgSupportBaseAdv(), PageBase {
    override fun getLayoutID(): Int = R.layout.page_main
    //override fun isUseEventBus(): Boolean = true

    override fun leavePage(): Boolean {
        return true
    }

    override fun initUI(savedInstanceState: Bundle?) {
        loadUI(savedInstanceState)
    }
}
