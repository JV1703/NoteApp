package com.example.noteapp.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ImageViewHolderBinding
import com.example.noteapp.utils.byteArrayToBitmap

class ImageAdapter :
    ListAdapter<ByteArray, ImageAdapter.ImageViewHolder>(DiffCallback) {

    class ImageViewHolder(val binding: ImageViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: ByteArray) {
            binding.iv.setImageBitmap(byteArrayToBitmap(image))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImageViewHolder(ImageViewHolderBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        holder.bind(image)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ByteArray>() {
        override fun areItemsTheSame(
            oldItem: ByteArray,
            newItem: ByteArray
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ByteArray,
            newItem: ByteArray
        ): Boolean {
            return oldItem == newItem
        }

    }
}