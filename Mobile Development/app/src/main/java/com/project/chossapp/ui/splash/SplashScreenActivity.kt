package com.project.chossapp.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.project.chossapp.databinding.ActivitySplashScreenBinding
import com.project.chossapp.ui.auth.AuthActivity
import com.project.chossapp.ui.onboarding.OnboardingActivity
import com.project.chossapp.util.setAppLocale

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        binding.ivLogo.animate().apply {
            duration = 1500
            alpha(1f)
        }.withEndAction {
            val destination =
                if (onBoardingFinished()) AuthActivity::class.java else OnboardingActivity::class.java
            startActivity(
                Intent(this@SplashScreenActivity, destination)
            )
            finish()
        }
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    override fun onStart() {
        super.onStart()
        val sharedPref = getSharedPreferences("language", Context.MODE_PRIVATE)
        val language = sharedPref.getString("language", "in")
        if (language == "en") {
            setAppLocale("en", this)
        }else {
            setAppLocale("id", this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}