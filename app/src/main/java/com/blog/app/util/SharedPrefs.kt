package com.blog.app.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPrefs {

    private val NAME = "shared_pref_local_db"
    private val gson = Gson()
    private var mSharedPref: SharedPreferences? = null

    fun init(context: Context) {
        if (mSharedPref == null) mSharedPref =
            context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    /** Save String Data **/
    fun write(key: String?, value: String?) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    /** Read String Data **/
    fun read(key: String?, defValue: String?): String? {
        return mSharedPref!!.getString(key, defValue)
    }
}
