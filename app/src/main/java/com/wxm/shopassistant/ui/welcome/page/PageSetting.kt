package com.wxm.shopassistant.ui.welcome.page

import android.os.Bundle
import com.wxm.shopassistant.R
import com.wxm.shopassistant.data.event.ChangePage
import com.wxm.shopassistant.ui.setting.TFSettingMain
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import wxm.KeepAccount.ui.setting.TFSettingBase
import wxm.KeepAccount.ui.setting.TFSettingCheckVersion
import wxm.androidutil.ui.frg.FrgSupportSwitcher

/**
 * for welcome
 * Created by WangXM on 2016/12/7.
 */
class PageSetting : FrgSupportSwitcher<TFSettingBase>(), PageBase {
    private val mTFCheckVer = TFSettingCheckVersion()
    private val mTFMain = TFSettingMain()

    init {
        setupFrgID(R.layout.page_empty, R.id.fl_page)
    }

    override fun isUseEventBus(): Boolean = true

    override fun setupFragment(savedInstanceState: Bundle?) {
        addChildFrg(mTFMain)
        addChildFrg(mTFCheckVer)
    }

    override fun leavePage(): Boolean {
        if(hotPage !== mTFMain)    {
            switchToPage(mTFMain)
            return false
        }

        return true
    }

    /**
     * handler for DB data change
     * @param event     for event
     */
    @Suppress("UNUSED_PARAMETER", "unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChangePageEvent(event: ChangePage) {
        when(event.JavaClassName)   {
            TFSettingCheckVersion::class.java.name ->    {
                switchToPage(mTFCheckVer)
            }
        }
    }
}
