package com.corona.awareness.activities

import android.os.Bundle
import com.corona.awareness.R
import com.corona.awareness.databinding.ActivityAboutCoronaBinding

class AboutCoronaActivity : BaseActivity() {

    private lateinit var bindingView: ActivityAboutCoronaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_about_corona)
        setUpToolBar()
    }

    private fun setUpToolBar() {
        setSupportActionBar(bindingView.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = ""
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
