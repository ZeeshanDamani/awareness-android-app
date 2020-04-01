package com.corona.awareness.activities

import android.os.Bundle
import com.corona.awareness.R
import com.corona.awareness.databinding.ActivityWhoGuidelinesBinding

class WhoGuidelinesActivity : BaseActivity() {

    private lateinit var bindingView: ActivityWhoGuidelinesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_who_guidelines)
        setUpToolBar()
        bindingView.whoGuidelinesWebView.loadUrl("https://www.who.int/health-topics/coronavirus")
    }

    private fun setUpToolBar() {
        setSupportActionBar(bindingView.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "W.H.O. Guidance"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
