package com.wxm.shopassistant.util

/**
 * @author      WangXM
 * @version     create：2018/5/24
 */

fun<T> T.let1(term:(T)->Unit){
    term(this)
}