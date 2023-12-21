package com.project.chossapp.ui.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.chossapp.databinding.ActivityShopBinding
import com.project.chossapp.ui.shop.adapter.ShopAdapter
import com.project.chossapp.util.listShop

class ShopActivity : AppCompatActivity() {

    private var _binding: ActivityShopBinding? = null
    private val binding get() = _binding!!
    private val shopAdapter by lazy { ShopAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvShop.apply {
            adapter = shopAdapter
            layoutManager = LinearLayoutManager(this@ShopActivity)
        }

        shopAdapter.differ.submitList(listShop)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}