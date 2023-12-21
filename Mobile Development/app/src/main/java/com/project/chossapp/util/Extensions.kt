package com.project.chossapp.util

import com.project.chossapp.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Locale

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

@SuppressLint("InflateParams")
fun Fragment.pickLanguage(
    onLanguagePick: (String) -> Unit
) {
    val dialog = Dialog(requireContext(), android.R.style.Theme_Dialog)
    val view = layoutInflater.inflate(R.layout.pick_language, null)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(view)
    dialog.window?.setGravity(Gravity.BOTTOM)
    dialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()

    val btnIndo = view.findViewById<LinearLayout>(R.id.layout_indonesia)
    val btnEnglish = view.findViewById<LinearLayout>(R.id.layout_english)

    btnIndo.setOnClickListener {
        onLanguagePick("in")
        dialog.dismiss()
    }

    btnEnglish.setOnClickListener {
        onLanguagePick("en")
        dialog.dismiss()
    }
}

fun Activity.setAppLocale(languageFromPreference: String?, context: Context)
{

    if (languageFromPreference != null) {
        val resources = context.resources
        val dm = resources.displayMetrics
        val config = resources.configuration
        config.setLocale(Locale(languageFromPreference.toLowerCase(Locale.ROOT)))
        resources.updateConfiguration(config, dm)

        val sharedPref = getSharedPreferences("language", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("languange", languageFromPreference)
        editor.apply()
    }
}