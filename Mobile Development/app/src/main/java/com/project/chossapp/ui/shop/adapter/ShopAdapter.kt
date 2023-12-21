package com.project.chossapp.ui.shop.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.chossapp.databinding.ItemShopBinding
import com.project.chossapp.data.model.Shop

class ShopAdapter: RecyclerView.Adapter<ShopAdapter.RecommendationViewHolder>() {

    inner class RecommendationViewHolder(val binding: ItemShopBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(shop: Shop) {
            with(binding) {
                val maxLength = 18
                val ellipsis = "..."
                val name = if (shop.name.length > 15) {
                    shop.name.take(maxLength - ellipsis.length) + ellipsis
                } else shop.name
                tvShopName.text = name
                tvClothName.text = shop.category
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Shop>() {
        override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        return RecommendationViewHolder(
            ItemShopBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

        val context = holder.itemView.context
        holder.binding.btnShopee.setOnClickListener {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
            )
        }
    }
}