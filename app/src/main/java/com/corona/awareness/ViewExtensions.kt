package com.corona.awareness

import android.widget.EditText

fun EditText.setTextIfAny(string: String?) {
    if (!string.isNullOrBlank()) {
        setText(string)
    }
}