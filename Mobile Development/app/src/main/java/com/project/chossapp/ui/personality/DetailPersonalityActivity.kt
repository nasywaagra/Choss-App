package com.project.chossapp.ui.personality

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.project.chossapp.databinding.ActivityDetailPersonalityBinding
import com.project.chossapp.data.model.Personality
import com.project.chossapp.ui.recommendation.RecommendationActivity
import com.project.chossapp.util.Dimen.PERSONALITY

@Suppress("DEPRECATION")
class DetailPersonalityActivity : AppCompatActivity() {

    private var _binding: ActivityDetailPersonalityBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailPersonalityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PERSONALITY, Personality::class.java)
        } else {
            intent.getParcelableExtra(PERSONALITY)
        }

        data?.let { personality ->
            with(binding) {
                tvPersonality.text = personality.name
                tvDescription.text = personality.description
                ivPersonality.setImageResource(personality.img)
            }
        }

        binding.btnGetRecommendation.setOnClickListener {
            val radioGroup = binding.radioGroup.checkedRadioButtonId
            val selectedGender = findViewById<RadioButton>(radioGroup)
            startActivity(
                Intent(this@DetailPersonalityActivity, RecommendationActivity::class.java).also {
                    it.putExtra("gender", selectedGender.text)
                }
            )
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}