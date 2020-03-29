package com.corona.awareness

import android.app.Application
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.helper.kotlin.Constants
import com.corona.awareness.model.login.loginResponse

class Awareness : Application(){

    companion object {

        @JvmField
        var appInstance: Awareness? = null
        @JvmField
        var loginData: loginResponse? = null



        @JvmStatic fun getAppInstance(): Awareness {
            return appInstance as Awareness
        }

        @JvmStatic fun getLoginData(): loginResponse? {
            loginData = AppSharedPreferences.get(Constants.LOGIN_OBJECT)
            if(loginData != null){
                return loginData as loginResponse
            }
            return null
        }


    }

    override fun onCreate() {
        super.onCreate()

        appInstance = this;

    }

}