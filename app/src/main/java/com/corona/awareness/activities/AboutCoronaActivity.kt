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

        bindingView.labelMoreInfo.setOnClickListener(this)
    }

    override fun onClick(view: View?) {

        if(view?.id == R.id.label_more_info){
            moreInfoCheck = true
            bindingView.labelAboutCorona.visibility = View.GONE
            bindingView.labelMoreInfo.visibility = View.GONE
            bindingView.aboutWebView.visibility = View.VISIBLE
            bindingView.aboutWebView.loadUrl("https://www.who.int/health-topics/coronavirus")
        }

    }

    override fun onBackPressed() {
        if(moreInfoCheck){
            moreInfoCheck = false
            bindingView.labelAboutCorona.visibility = View.VISIBLE
            bindingView.labelMoreInfo.visibility = View.VISIBLE
            bindingView.aboutWebView.visibility = View.GONE

        }else{
            super.onBackPressed()
        }

    }


}
