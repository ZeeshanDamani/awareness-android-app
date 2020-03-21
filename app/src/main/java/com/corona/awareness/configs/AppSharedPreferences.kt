package com.corona.awareness.configs

import android.content.Context
import android.content.SharedPreferences

 object AppSharedPreferences {
    private val sharedPrefFile = "myAppSharedPreferences"
    private lateinit var sharedPreferences: SharedPreferences;

    fun init(context : Context ){
        sharedPreferences = context.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("userPhone","1234");
        editor.putString("userPassword","1234");
        editor.apply()
        editor.commit()
    }

    fun getConfig(key : String): String? {
        return sharedPreferences.getString(key, key)
    }
}