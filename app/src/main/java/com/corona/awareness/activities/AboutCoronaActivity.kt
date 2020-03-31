package com.corona.awareness.activities

import android.os.Bundle
import android.view.View
import com.corona.awareness.R
import com.corona.awareness.databinding.ActivityAboutCoronaBinding

class AboutCoronaActivity : BaseActivity(),View.OnClickListener {

    private  var moreInfoCheck: Boolean = false
    private lateinit var bindingView: ActivityAboutCoronaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("About Corona Virus")
        bindingView = setContentViewDataBinding(R.layout.activity_about_corona)

    }

    override fun onClick(view: View?) {

    }



}
