package com.wxm.shopassistant.ui.login


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.wxm.shopassistant.R
import com.wxm.shopassistant.define.GlobalDef
import com.wxm.shopassistant.ui.welcome.ACWelcome
import com.wxm.shopassistant.util.AppUtil
import com.wxm.shopassistant.util.ToolUtil

import kotterknife.bindView
import wxm.androidutil.ui.frg.FrgSupportBaseAdv
import java.util.concurrent.TimeUnit

/**
 * for login
 * Created by WangXM on 2016/11/29.
 */
class FrgLogin : FrgSupportBaseAdv() {
    private val mPBLoginProgress: ProgressBar  by bindView(R.id.login_progress)
    private val mETEmail: AutoCompleteTextView by bindView(R.id.email)
    private val mETPassword: EditText by bindView(R.id.password)
    private val mBTEmailSignIn: Button by bindView(R.id.email_sign_in_button)
    private val mBTEmailRegister: Button by bindView(R.id.email_register_button)
    private val mBTDefUsrLogin: Button by bindView(R.id.bt_def_usr_login)
    private val mLLLogin: LinearLayout by bindView(R.id.ll_login)

    override fun getLayoutID(): Int = R.layout.frg_login

    override fun initUI(bundle: Bundle?) {
        mBTEmailSignIn.setOnClickListener { _ -> attemptLogin() }

        mBTEmailRegister.setOnClickListener { _ ->
            //startActivityForResult(Intent(activity, ACAddUsr::class.java), 1)
        }

        mBTDefUsrLogin.setOnClickListener { _ -> doLogin(GlobalDef.DEF_USR_NAME, GlobalDef.DEF_USR_PWD) }
    }

    /// PRIVATE BEGIN
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid mETEmail, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        // Reset errors.
        mETEmail.error = null
        mETPassword.error = null

        // Store values at the time of the login attempt.
        val email = mETEmail.text.toString()
        val password = mETPassword.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mETPassword.error = getString(R.string.error_invalid_password)
            focusView = mETPassword
            cancel = true
        }

        // Check for a valid mETEmail address.
        if (TextUtils.isEmpty(email)) {
            mETEmail.error = getString(R.string.error_field_required)
            focusView = mETEmail
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            doLogin(email, password)
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length >= 4
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        val shortAnimTime = resources
                .getInteger(android.R.integer.config_shortAnimTime)

        mLLLogin.visibility = if (show) View.GONE else View.VISIBLE
        mLLLogin.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mLLLogin.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        mPBLoginProgress.visibility = if (show) View.VISIBLE else View.GONE
        mPBLoginProgress.animate().setDuration(shortAnimTime.toLong())
                .alpha((if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        mPBLoginProgress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    /**
     * do login in background
     * @param usr       usr name
     * @param pwd       usr password
     */
    private fun doLogin(usr: String, pwd: String) {
        showProgress(true)
        val bRet = ToolUtil.callInBackground(
                        { AppUtil.usrUtil.loginByUsr(usr, pwd)}, false,
                        TimeUnit.SECONDS, 3)
        showProgress(false)

        if(bRet)    {
            startActivityForResult(Intent(activity, ACWelcome::class.java), 1)
        } else  {
            mETPassword.error = getString(R.string.error_incorrect_password)
            mETPassword.requestFocus()
        }
    }
    /// PRIVATE END
}
