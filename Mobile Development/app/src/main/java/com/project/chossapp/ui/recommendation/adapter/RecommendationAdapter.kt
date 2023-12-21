package com.project.chossapp.ui.recommendation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.chossapp.data.model.Clothes
import com.project.chossapp.databinding.ItemCardRecommendationBinding

class RecommendationAdapter(val clothes: Clothes): RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    inner class RecommendationViewHolder(val binding: ItemCardRecommendationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(clothes: Int?) {
            with(binding) {
                clothes?.let {
                    ivCloth.setImageResource(clothes)
                }
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        return RecommendationViewHolder(
            ItemCardRecommendationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClick?.invoke(clothes)
        }
    }

    var onClick: ((Clothes) -> Unit)? = null
}