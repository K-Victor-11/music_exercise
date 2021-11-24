package com.exercise.music_exercise.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtils {
    val SHARED_NAME:String = "music_exercises_info"
    private var context: Context? = null
    val prefs: SharedPreferences by lazy {
        context!!.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    companion object{
        private var instance: PreferenceUtils? = null
        @JvmStatic
        @Synchronized
        open fun getInstance(ctx: Context): PreferenceUtils {
            if (instance == null) {
                instance = PreferenceUtils(ctx)
            }
            return instance!!
        }
    }

    constructor(ctx: Context) {
        context = ctx.applicationContext
    }

    fun setString(key:String, value:String){
        var editor: SharedPreferences.Editor = instance!!.prefs.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun getString(key:String, defaultValue:String?):String{
        return instance!!.prefs.getString(key, defaultValue).toString()
    }

    fun setBoolean(key:String, value:Boolean){
        var editor: SharedPreferences.Editor = instance!!.prefs.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getBoolean(key:String, defaultValue:Boolean):Boolean{
        return instance!!.prefs.getBoolean(key, defaultValue)
    }

    fun setInt(key:String, value:Int){
        var editor: SharedPreferences.Editor = instance!!.prefs.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun getInt(key:String, defaultValue:Int): Int {
        return instance!!.prefs.getInt(key, defaultValue)
    }
}