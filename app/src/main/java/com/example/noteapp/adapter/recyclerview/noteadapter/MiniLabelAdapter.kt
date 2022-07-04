package com.example.noteapp.adapter.recyclerview.noteadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.databinding.MiniLabelViewHolderBinding

class MiniLabelAdapter : ListAdapter<Label, MiniLabelAdapter.MiniLabelViewHolder>(DiffCallback) {

    class MiniLabelViewHolder(val binding: MiniLabelViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(label: Label) {
            binding.labelName.text = label.labelName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniLabelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MiniLabelViewHolder(
            MiniLabelViewHolderBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MiniLabelViewHolder, position: Int) {
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