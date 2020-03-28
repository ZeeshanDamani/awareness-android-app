package com.corona.awareness.helper.kotlin

import android.content.Context

object Utils{

    fun openProgressDialog(context: Context): CustomProgressBar{

        val progressBar = CustomProgressBar()
        progressBar.show(context,"Please Wait...")
        return progressBar
    }


}