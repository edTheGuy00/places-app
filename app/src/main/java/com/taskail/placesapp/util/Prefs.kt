package com.taskail.placesapp.util

/**
 *Created by ed on 4/15/18.
 */

fun saveUserPhoneId(user: String){
    setString("phoneId", user)
}

fun getUserPhoneId(): String?{
    return getString("phoneId", "none")
}