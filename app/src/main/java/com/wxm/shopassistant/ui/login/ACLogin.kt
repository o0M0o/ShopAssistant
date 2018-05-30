package com.wxm.shopassistant.ui.login

import android.os.Bundle
import android.os.Process
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.wxm.shopassistant.R
import wxm.androidutil.ui.activity.ACSwitcherActivity

/**
 * A login screen that offers login via email/password.
 */
class ACLogin : ACSwitcherActivity<FrgLogin>() {
    override fun getBackIconRID(): Int = R.drawable.ic_leave_white

    override fun leaveActivity() {
        Toast.makeText(this, R.string.show_quit_app,
                Toast.LENGTH_SHORT).show()

        try {
            Thread.sleep(1000)
        } finally {

            Process.killProcess(Process.myPid())
        }

    }

    override fun setupFragment(p0: Bundle?) {
        addFragment(FrgLogin())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.mu_login, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_help -> {
                /*
                val intent = Intent(this, ACHelp::class.java)
                intent.putExtra(ACHelp.STR_HELP_TYPE, ACHelp.STR_HELP_MAIN)

                startActivityForResult(intent, 1)
                */
            }

            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}

