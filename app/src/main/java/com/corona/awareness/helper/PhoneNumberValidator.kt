package com.corona.awareness.helper

object PhoneNumberValidator {
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return "^92\\d{10}".toRegex().find(phoneNumber)?.value?.isNotBlank() ?: false
    }
}