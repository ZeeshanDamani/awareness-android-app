package com.corona.awareness.activities

import android.content.Intent
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseActivity: AppCompatActivity() {
    protected open fun <T : ViewDataBinding> setContentViewDataBinding(@LayoutRes layoutResId: Int): T {
        return DataBindingUtil.setContentView(this, layoutResId)
    }

    fun goToLginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

    fun goToDashboardActivity() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finishAffinity()
    }
}
