package com.example.firebaseapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("CommitPrefEdits")
class SharedPreferenceHelper constructor(private val context: Context) {
    init {
        instance = this
        sharedPreferences = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences!!.edit()
    }

    companion object {
        private const val APP_NAME = "couchvibes"
        private const val USER_NAME = "userId"

        private var instance: SharedPreferenceHelper? = null

        private var sharedPreferences: SharedPreferences? = null
        private var sharedPreferencesEditor: SharedPreferences.Editor? = null
        fun getInstance(): SharedPreferenceHelper? {
            if (instance == null) {
                throw NullPointerException("SharedHelper was not initialized!")
            }
            return instance
        }
    }
    fun init() {
        if (instance == null)
            instance = SharedPreferenceHelper(context)
    }
    private fun delete(key: String) {
        if (sharedPreferences!!.contains(key)) {
            sharedPreferencesEditor!!.remove(key).commit()
        }
    }
    private fun savePref(key: String, value: Any?) {
        delete(key)
        if (value is Boolean) {
            sharedPreferencesEditor!!.putBoolean(key, (value as Boolean?)!!)
        } else if (value is Int) {
            sharedPreferencesEditor!!.putInt(key, (value as Int?)!!)
        } else if (value is Float) {
            sharedPreferencesEditor!!.putFloat(key, (value as Float?)!!)
        } else if (value is Long) {
            sharedPreferencesEditor!!.putLong(key, (value as Long?)!!)
        } else if (value is String) {
            sharedPreferencesEditor!!.putString(key, value as String?)
        } else if (value is Enum<*>) {
            sharedPreferencesEditor!!.putString(key, value.toString())
        } else if (value != null) {
            throw RuntimeException("Attempting to save non-primitive preference")
        }
        sharedPreferencesEditor!!.commit()
    }

    private fun <T> getPref(key: String): T? {
        return sharedPreferences!!.all[key] as T?
    }
    private fun <T> getPref(key: String, defValue: T): T {
        val returnValue = sharedPreferences!!.all[key] as T?
        return returnValue ?: defValue
    }

    fun clearAll() {
        sharedPreferencesEditor!!.clear().apply()

    }

    fun getUserName(): String? {
        return getPref<String>(USER_NAME)
    }

    fun saveUserName(name: String?) {
        savePref(USER_NAME, name)
    }

}