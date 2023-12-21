package com.project.chossapp.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.project.chossapp.R
import com.project.chossapp.databinding.ActivityOnboardingBinding
import com.project.chossapp.ui.auth.AuthActivity
import com.project.chossapp.ui.onboarding.adapter.OnBoardingItem
import com.project.chossapp.ui.onboarding.adapter.OnboardingAdapter
import com.project.chossapp.util.hide
import com.project.chossapp.util.visible

class OnboardingActivity : AppCompatActivity() {

    private var _binding: ActivityOnboardingBinding? = null
    private val binding get() = _binding!!
    private lateinit var onBoardingAdapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupOnBoarding()
        binding.viewPager2.adapter = onBoardingAdapter

        setupOnBoarding()
        setupBoardingIndicators()
        setActiveIndicators(0)

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setActiveIndicators(position)
            }
        })

        binding.btnNext.setOnClickListener {
            if (binding.viewPager2.currentItem + 1 < onBoardingAdapter.itemCount) {
                binding.viewPager2.currentItem = binding.viewPager2.currentItem + 1
            } else {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                onBoardingFinished()
                finish()
            }
        }

        binding.btnBack.setOnClickListener {
            binding.viewPager2.currentItem = binding.viewPager2.currentItem - 1
        }
    }

    private fun setupBoardingIndicators() {
        val indicators = arrayOfNulls<ImageView>(onBoardingAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_indicator_active
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.viewpagerIndicator.addView(indicators[i])
        }
    }

    private fun setActiveIndicators(index: Int) {
        val childCount = binding.viewpagerIndicator.childCount
        for (i in 0 until childCount) {
            val imageView = binding.viewpagerIndicator[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_indicator_inactive
                    )
                )
            }
        }
        when (index) {
            0 -> {
                binding.btnNext.visible()
                binding.btnBack.hide()
                binding.btnNext.text = getString(R.string.next)
            }

            onBoardingAdapter.itemCount - 1 -> {
                binding.btnBack.visible()
                binding.btnNext.text = getString(R.string.get_started)
            }

            else -> {
                binding.btnNext.text = getString(R.string.next)
                binding.btnBack.visible()
            }
        }
    }

    private fun setupOnBoarding() {
        val onBoardingItems: MutableList<OnBoardingItem> = ArrayList()
        val onBoarding1 = OnBoardingItem(
            R.drawable.onboarding1,
            getString(R.string.onboarding_title_1),
            getString(R.string.onboarding_message_1)
        )
        val onBoarding2 = OnBoardingItem(
            R.drawable.onboarding2,
            getString(R.string.onboarding_title_2),
            getString(R.string.onboarding_message_2)
        )
        onBoardingItems.add(onBoarding1)
        onBoardingItems.add(onBoarding2)
        onBoardingAdapter = OnboardingAdapter()
        onBoardingAdapter.setNewItem(onBoardingItems)
    }

    private fun onBoardingFinished() {
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}