package com.corona.awareness.configs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

object AppSharedPreferences {

    lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun <T> put(`object`: T, key: String) {
        val jsonString = GsonBuilder().create().toJson(`object`)
        sharedPreferences.edit()?.putString(key, jsonString)?.apply()
    }

    inline fun <reified T> get(key: String): T? {
        val value = sharedPreferences.getString(key, null)
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

    inline fun <reified T> get(key: String, type: Type): T? {
        val value = sharedPreferences.getString(key, null)
        return GsonBuilder().create().fromJson(value, type)
    }

    private const val SHARED_PREF_FILE_NAME = "AppSharedPreferences"
}