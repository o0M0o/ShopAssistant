package com.wxm.shopassistant.define

/**
 * Action in welcome page
 * Created by WangXM on 2018/2/19.
 */
enum class EDBChange(val changeType: String) {
    CREATE(GlobalDef.DB_CREATE),
    MODIFY(GlobalDef.DB_MODIFY),
    DELETE(GlobalDef.DB_DELETE);
}
