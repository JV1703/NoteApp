package com.example.noteapp.adapter.recyclerview.labeladapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.databinding.NavViewLabelViewHolderBinding

class NavViewLabelAdapter :
    ListAdapter<Label, NavViewLabelAdapter.NavViewLabelViewHolder>(DiffCallback) {

    class NavViewLabelViewHolder(val binding: NavViewLabelViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(label: Label) {
            binding.label = label
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavViewLabelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NavViewLabelViewHolder(
            NavViewLabelViewHolderBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NavViewLabelViewHolder, position: Int) {
        val label = getItem(position)
        holder.bind(label)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Label>() {
        override fun areItemsTheSame(oldItem: Label, newItem: Label): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Label, newItem: Label): Boolean {
            return oldItem == newItem
        }
    }
}