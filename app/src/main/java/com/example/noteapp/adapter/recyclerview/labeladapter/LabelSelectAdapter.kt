package com.example.noteapp.adapter.recyclerview.labeladapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.databinding.LabelSelectorViewHolderBinding
import com.example.noteapp.ui.fragments.NoteViewModel

class LabelSelectAdapter(val noteViewModel: NoteViewModel) :
    ListAdapter<Label, LabelSelectAdapter.LabelSelectViewHolder>(DiffCallback) {

//    var labelList = arrayListOf<Label>()

    inner class LabelSelectViewHolder(val binding: LabelSelectorViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(label: Label) {
            binding.checkbox.text = label.labelName
            binding.checkbox.isChecked = noteViewModel.selectedLabels.contains(label)
            binding.labelRoot.setOnClickListener {
                binding.checkbox.performClick()
                labelList(binding.checkbox, label)
                Log.i("label_selector", "${binding.checkbox.text}: ${binding.checkbox.isChecked}")
                Log.i("label_selector", "${noteViewModel.selectedLabels}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelSelectViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LabelSelectViewHolder(
            LabelSelectorViewHolderBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    private fun labelList(checkBox: CheckBox, label: Label) {
        if (checkBox.isChecked) {
            noteViewModel.checkLabel(label)
        } else {
            noteViewModel.uncheckLabel(label)
        }
    }

    override fun onBindViewHolder(holder: LabelSelectViewHolder, position: Int) {
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