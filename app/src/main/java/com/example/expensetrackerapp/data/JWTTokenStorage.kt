package com.example.expensetrackerapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}
fun saveToken(context: Context, token: String) {
    val sharedPref = getEncryptedSharedPreferences(context)
    sharedPref.edit().putString("jwt_token", token).apply()
}

fun getToken(context: Context): String? {
    val sharedPref = getEncryptedSharedPreferences(context)
    return sharedPref.getString("jwt_token", null)
}

fun removeToken(context: Context) {
    val sharedPref = getEncryptedSharedPreferences(context)
    sharedPref.edit().remove("jwt_token").apply()
}

