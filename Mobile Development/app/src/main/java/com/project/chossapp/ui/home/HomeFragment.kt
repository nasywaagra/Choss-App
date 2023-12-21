package com.project.chossapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.chossapp.R
import com.project.chossapp.databinding.FragmentHomeBinding
import com.project.chossapp.ui.home.adapter.PhotoAdapter
import com.project.chossapp.ui.personality.PersonalityActivity
import com.project.chossapp.ui.shop.ShopActivity
import com.project.chossapp.util.pickLanguage
import com.project.chossapp.util.setAppLocale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val photoAdapter by lazy { PhotoAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.btnLanguage.setOnClickListener {
            handleLanguage()
        }
        binding.btnPersonality.setOnClickListener {
            startActivity(
                Intent(requireContext(), PersonalityActivity::class.java)
            )
        }
        binding.btnShopRecommendation.setOnClickListener {
            startActivity(
                Intent(requireContext(), ShopActivity::class.java)
            )
        }
    }

    private fun handleLanguage() {
        val sharedPref = requireActivity().getSharedPreferences("language", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        pickLanguage {
            if (it == "en") {
                requireActivity().setAppLocale("en", requireContext())
                editor.putString("language", "en")
                editor.apply()
                onDestroy()
            }else {
                requireActivity().setAppLocale("id", requireContext())
                editor.putString("language", "in")
                editor.apply()
                onDestroy()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvHome.apply {
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        val photos = mutableListOf(
            R.drawable.iv_clothes,
            R.drawable.iv_personality,
            R.drawable.iv_men_clothes
        )
        photoAdapter.differ.submitList(photos)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}