package com.corona.awareness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.corona.awareness.databinding.ActivityAboutCoronaBinding
import com.corona.awareness.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_about_corona.*

class AboutCoronaActivity : BaseActivity() {

    private lateinit var bindingView: ActivityAboutCoronaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_about_corona)
        bindingView.aboutWebView.loadUrl("https://www.who.int/health-topics/coronavirus")
    }


}
