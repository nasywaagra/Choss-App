package com.project.chossapp.data

import android.content.Context
import android.content.SharedPreferences
import com.project.chossapp.util.Dimen.EMAIL
import com.project.chossapp.util.Dimen.SESSION
import com.project.chossapp.util.Dimen.TOKEN

object Preferences {
    fun init(context: Context, name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun preferenceEditor(context: Context): SharedPreferences.Editor {
        val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        return sharedPref.edit()
    }

    fun saveToken(token: String, email: String, context: Context) {
        val editor = preferenceEditor(context)
        editor.putString(TOKEN, token)
        editor.putString(EMAIL, email)
        editor.apply()
    }

    fun logOut(context: Context) {
        val editor = preferenceEditor(context)
        editor.remove(TOKEN)
        editor.remove(EMAIL)
        editor.remove("status")
        editor.apply()
    }
}