package com.wxm.shopassistant.ui.welcome.page

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.theartofdev.edmodo.cropper.CropImage
import com.wxm.shopassistant.R
import com.wxm.shopassistant.util.AppUtil
import com.wxm.shopassistant.util.let1
import com.wxm.shopassistant.util.saveImage
import kotterknife.bindView
import wxm.androidutil.ui.dialog.DlgAlert
import wxm.androidutil.ui.frg.FrgSupportBaseAdv
import wxm.uilib.IconButton.IconButton
import java.io.File

/**
 * for welcome
 * Created by WangXM on 2016/12/7.
 */
class PageUsr : FrgSupportBaseAdv(), PageBase {
    // for ui
    private val mIVUsr: ImageView by bindView(R.id.iv_usr)
    private val mTVUsrName: TextView by bindView(R.id.tv_usr_name)
    private val mIBLogout: IconButton by bindView(R.id.ib_logout)

    private val mIBChangePwd: IconButton by bindView(R.id.ib_change_pwd)
    private val mCLInputPwd: ConstraintLayout by bindView(R.id.cl_input_pwd)
    private val mCLChangePwd: ConstraintLayout by bindView(R.id.cl_change_pwd)

    override fun getLayoutID(): Int = R.layout.page_usr
    //override fun isUseEventBus(): Boolean = true
    override fun leavePage(): Boolean = true

    override fun initUI(savedInstanceState: Bundle?) {
        mIBLogout.setOnClickListener(::onClick)
        mIVUsr.setOnClickListener(::onClick)

        mIBChangePwd.setColdOrHot(false)
        mCLInputPwd.visibility = View.GONE
        mIBChangePwd.setOnClickListener(::onClick)

        view!!.setOnClickListener(::onClick)

        autoScroll(R.id.te_old_pwd, mCLChangePwd)
        autoScroll(R.id.te_new_pwd, mCLChangePwd)
        autoScroll(R.id.te_repeat_new_pwd, mCLChangePwd)

        loadUI(savedInstanceState)
    }

    override fun loadUI(savedInstanceState: Bundle?) {
        loadUsrInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                saveImage(result.uri).let1 {
                    if(it.isNotEmpty()) {
                        if(AppUtil.usrUtil.changeIcon(AppUtil.curUsr!!, it)) {
                            loadUsrInfo()
                        }
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                DlgAlert.showAlert(activity!!, R.string.dlg_erro, result.error.toString())
            }
        }
    }

    fun onClick(vw: View) {
        when (vw.id) {
            R.id.ib_logout -> doLogout(activity!!)
            R.id.ib_change_pwd -> {
                if (mIBChangePwd.isHot) {
                    mCLInputPwd.visibility = View.GONE
                    mIBChangePwd.setColdOrHot(false)
                } else {
                    mCLInputPwd.visibility = View.VISIBLE
                    mIBChangePwd.setColdOrHot(true)
                }
            }

            R.id.iv_usr -> {
                CropImage.activity().start(context!!, this)
            }

            else -> {
                view!!.let1 {
                    if (0 != it.scrollY) {
                        it.scrollY = 0
                    }
                }
            }
        }
    }

    private fun loadUsrInfo()   {
        AppUtil.curUsr?.let1 {
            mIVUsr.setImageURI(Uri.fromFile(File(it.iconPath)))
            mTVUsrName.text = it.name
        }
    }


    /**
     * auto scroll to view [v] to top of [topVW]
     */
    private fun autoScroll(v: Any, topVW: Any) {
        val vwHome = view!!

        val getVWObj = { vw: Any ->
            when (vw) {
                is Int -> vwHome.findViewById(vw)!!
                is View -> vw
                else -> throw IllegalStateException("${vw.javaClass.name} not view!")
            }
        }

        val getTop = { vw: Any ->
            Rect().apply { getVWObj(vw).getGlobalVisibleRect(this) }.top
        }

        getVWObj(v).setOnFocusChangeListener({ _: View, hasFocus: Boolean ->
            vwHome.scrollY = if (hasFocus) {
                getTop(topVW) - getTop(vwHome)
            } else 0
        })
    }
}
