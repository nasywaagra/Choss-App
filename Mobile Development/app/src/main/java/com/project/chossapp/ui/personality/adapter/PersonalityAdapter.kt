package com.project.chossapp.ui.personality.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.chossapp.databinding.ItemPersonalityBinding
import com.project.chossapp.data.model.Personality
import com.project.chossapp.ui.personality.DetailPersonalityActivity
import com.project.chossapp.util.Dimen.PERSONALITY

class PersonalityAdapter: RecyclerView.Adapter<PersonalityAdapter.PersonalityViewHolder>() {

    inner class PersonalityViewHolder(val binding: ItemPersonalityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(personality: Personality) {
            with(binding) {
                tvPersonality.text = personality.name
                ivPersonality.setImageResource(personality.img)
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Personality>() {
        override fun areItemsTheSame(oldItem: Personality, newItem: Personality): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Personality, newItem: Personality): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalityViewHolder {
        return PersonalityViewHolder(
            ItemPersonalityBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: PersonalityViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            context.startActivity(
                Intent(context, DetailPersonalityActivity::class.java).also {
                    it.putExtra(PERSONALITY, item)
                }
            )
        }
    }
}