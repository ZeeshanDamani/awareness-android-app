package com.corona.awareness

import android.app.Application
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.helper.kotlin.Constants
import com.corona.awareness.network.model.LoginResponseModel

class Awareness : Application() {

    companion object {

        @JvmField
        var appInstance: Awareness? = null
        @JvmField
        var loginData: LoginResponseModel? = null

        @JvmStatic
        fun getAppInstance(): Awareness {
            return appInstance as Awareness
        }

        @JvmStatic
        fun getLoginData(): LoginResponseModel? {
            loginData = AppSharedPreferences.get(Constants.LOGIN_OBJECT)
            return loginData
        }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        AppSharedPreferences.init(this)
    }

}