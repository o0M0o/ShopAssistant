package wxm.KeepAccount.ui.setting

import wxm.androidutil.ui.frg.FrgSupportBaseAdv

/**
 * for base setting
 * Created by WangXM on 2016/10/10.
 */
abstract class TFSettingBase : FrgSupportBaseAdv() {
    /**
     * check whether setting is changed
     * @return      if setting changed return true else false
     */
    var isSettingDirty = false
        protected set

    /**
     * save setting
     */
    abstract fun updateSetting()
}
