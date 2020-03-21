package com.corona.awareness

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseActivity: AppCompatActivity() {
    protected open fun <T : ViewDataBinding> setContentViewDataBinding(@LayoutRes layoutResId: Int): T {
        return DataBindingUtil.setContentView(this, layoutResId)
    }
}