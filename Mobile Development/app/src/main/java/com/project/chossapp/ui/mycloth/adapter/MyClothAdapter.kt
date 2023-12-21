package com.project.chossapp.ui.mycloth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.chossapp.databinding.ItemMyclothBinding
import com.project.chossapp.data.model.Cloth

class MyClothAdapter: RecyclerView.Adapter<MyClothAdapter.MyClothViewHolder>() {

    inner class MyClothViewHolder(val binding: ItemMyclothBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cloth: Cloth) {
            binding.tvMycloth.text = cloth.name
            binding.ivMycloth.setImageResource(cloth.photo)
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Cloth>() {
        override fun areItemsTheSame(oldItem: Cloth, newItem: Cloth): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Cloth, newItem: Cloth): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyClothViewHolder {
        return MyClothViewHolder(
            ItemMyclothBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: MyClothViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

        holder.binding.btnDeleteCloth.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    var onClick: ((Cloth) -> Unit)? = null
}