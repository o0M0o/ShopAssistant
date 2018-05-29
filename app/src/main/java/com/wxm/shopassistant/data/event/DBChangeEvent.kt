package com.wxm.shopassistant.data.event

import com.wxm.shopassistant.define.EDBChange

/**
 * after DB data changed, use this as event parameter
 * Created by WangXM on 2017/2/14.
 */
data class DBChangeEvent(val mk:List<Any>, val type:EDBChange)
