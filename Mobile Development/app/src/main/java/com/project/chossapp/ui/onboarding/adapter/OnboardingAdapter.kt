package com.project.chossapp.ui.onboarding.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.chossapp.databinding.ViewPagerContainerBinding

class OnboardingAdapter: RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    private var onBoardingItems: MutableList<OnBoardingItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = ViewPagerContainerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OnboardingViewHolder(binding)
    }

    override fun getItemCount(): Int = onBoardingItems.size

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        with(holder) {
            with(onBoardingItems[position]) {
                binding.tvOnboardingTitle.text = this.title
                binding.tvOnboardingMessage.text = this.message
                binding.imageView.setImageResource(this.photo)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNewItem(data: List<OnBoardingItem>) {
        onBoardingItems.clear()
        onBoardingItems.addAll(data)
        notifyDataSetChanged()
    }

    inner class OnboardingViewHolder(val binding: ViewPagerContainerBinding): RecyclerView.ViewHolder(binding.root)
}

data class OnBoardingItem(
    val photo: Int,
    val title: String,
    val message: String
)