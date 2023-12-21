package com.project.chossapp.ui.recommendation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.chossapp.data.model.Clothes
import com.project.chossapp.databinding.ActivityRecommendationBinding
import com.project.chossapp.ui.recommendation.adapter.RecommendationAdapter
import com.project.chossapp.util.Dimen
import com.project.chossapp.util.bottom
import com.project.chossapp.util.menBottom
import com.project.chossapp.util.menTop
import com.project.chossapp.util.top

class RecommendationActivity : AppCompatActivity() {

    private var _binding: ActivityRecommendationBinding? = null
    private val binding get() = _binding!!
    private lateinit var topAdapter1: RecommendationAdapter
    private lateinit var topAdapter2: RecommendationAdapter
    private lateinit var topAdapter3: RecommendationAdapter
    private lateinit var bottomAdapter1: RecommendationAdapter
    private lateinit var bottomAdapter2: RecommendationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPage()
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupPage() {
        val gender = intent.getStringExtra("gender") ?: "Women"
        var dataDummyTop = top
        var dataDummyBottom = bottom

        if (gender == "Men") {
            dataDummyTop = menTop
            dataDummyBottom = menBottom
        }

        topAdapter1 = RecommendationAdapter(dataDummyTop[0])
        topAdapter2 = RecommendationAdapter(dataDummyTop[1])
        topAdapter3 = RecommendationAdapter(dataDummyTop[2])
        bottomAdapter1 = RecommendationAdapter(dataDummyBottom[0])
        bottomAdapter2 = RecommendationAdapter(dataDummyBottom[1])

        binding.tvTopName1.text = dataDummyTop[0].name
        binding.tvTopName2.text = dataDummyTop[1].name
        binding.tvTopName3.text = dataDummyTop[2].name
        binding.tvBottomName1.text = dataDummyBottom[0].name
        binding.tvBottomName2.text = dataDummyBottom[1].name

        binding.vpTop1.adapter = topAdapter1
        binding.vpTop2.adapter = topAdapter2
        binding.vpTop3.adapter = topAdapter3
        binding.vpBottom1.adapter = bottomAdapter1
        binding.vpBottom2.adapter = bottomAdapter2

        topAdapter1.differ.submitList(dataDummyTop[0].photos)
        topAdapter2.differ.submitList(dataDummyTop[1].photos)
        topAdapter3.differ.submitList(dataDummyTop[2].photos)
        bottomAdapter1.differ.submitList(dataDummyBottom[0].photos)
        bottomAdapter2.differ.submitList(dataDummyBottom[1].photos)

        binding.ciTop1.setViewPager(binding.vpTop1)
        binding.ciTop2.setViewPager(binding.vpTop2)
        binding.ciTop3.setViewPager(binding.vpTop3)
        binding.ciBottom1.setViewPager(binding.vpBottom1)
        binding.ciBottom2.setViewPager(binding.vpBottom2)

        topAdapter1.onClick = { toDetail(it, 1) }
        topAdapter2.onClick = { toDetail(it, 2) }
        topAdapter3.onClick = { toDetail(it, 3) }
        bottomAdapter1.onClick = { toDetail(it, 1) }
        bottomAdapter2.onClick = { toDetail(it, 2) }
    }

    private fun toDetail(clothes: Clothes, index: Int) {
        startActivity(
            Intent(this, DetailRecommendationActivity::class.java).also {
                it.putExtra(Dimen.CLOTH, clothes)
                it.putExtra("index", index)
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}