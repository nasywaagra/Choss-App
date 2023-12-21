package com.project.chossapp.ui.personality

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.chossapp.databinding.ActivityPersonalityBinding
import com.project.chossapp.ui.personality.adapter.PersonalityAdapter
import com.project.chossapp.util.analyst
import com.project.chossapp.util.diplomats
import com.project.chossapp.util.explorers
import com.project.chossapp.util.sentinels

class PersonalityActivity : AppCompatActivity() {

    private var _binding: ActivityPersonalityBinding? = null
    private val binding get() = _binding!!
    private val analystAdapter by lazy { PersonalityAdapter() }
    private val diplomatsAdapter by lazy { PersonalityAdapter() }
    private val sentinelAdapter by lazy { PersonalityAdapter() }
    private val explorerAdapter by lazy { PersonalityAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPersonalityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnPersonality.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.16personalities.com/id/tes-kepribadian")
                )
            )
        }
    }

    private fun setupRecyclerView() {
        binding.rvAnalyst.apply {
            adapter = analystAdapter
            layoutManager =
                LinearLayoutManager(this@PersonalityActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvDiplomats.apply {
            adapter = diplomatsAdapter
            layoutManager =
                LinearLayoutManager(this@PersonalityActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvExplorers.apply {
            adapter = explorerAdapter
            layoutManager =
                LinearLayoutManager(this@PersonalityActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvSentinels.apply {
            adapter = sentinelAdapter
            layoutManager =
                LinearLayoutManager(this@PersonalityActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        analystAdapter.differ.submitList(analyst)
        diplomatsAdapter.differ.submitList(diplomats)
        explorerAdapter.differ.submitList(explorers)
        sentinelAdapter.differ.submitList(sentinels)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}