package com.corona.awareness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.corona.awareness.databinding.ActivityAboutCoronaBinding
import com.corona.awareness.databinding.ActivityWhoGuidelinesBinding

class WhoGuidelinesActivity : BaseActivity() {

    private lateinit var bindingView: ActivityWhoGuidelinesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_who_guidelines)
        bindingView.whoGuidelinesWebView.loadUrl("https://www.who.int/health-topics/coronavirus")
    }
}
