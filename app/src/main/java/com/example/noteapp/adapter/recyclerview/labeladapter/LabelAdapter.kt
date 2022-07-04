package com.example.noteapp.adapter.recyclerview.labeladapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.BaseApplication
import com.example.noteapp.R
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.databinding.LabelViewHolderBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LabelAdapter(activity: Activity) :
    ListAdapter<Label, LabelAdapter.LabelViewHolder>(DiffCallback) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    val noteDao = (activity.application as BaseApplication).noteDatabase.noteDao()

    inner class LabelViewHolder(val binding: LabelViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(label: Label) {
            binding.labelTextField.editText?.setText(label.labelName, TextView.BufferType.EDITABLE)
            labelVhIconFocus(binding.labelTextField, label)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LabelViewHolder(LabelViewHolderBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        val currentLabel = getItem(position)
        holder.bind(currentLabel)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Label>() {
        override fun areItemsTheSame(oldItem: Label, newItem: Label): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Label, newItem: Label): Boolean {
            return oldItem == newItem
        }
    }

    fun labelVhIconFocus(textInputLayout: TextInputLayout, label: Label) {
        textInputLayout.editText?.setOnFocusChangeListener { view, boolean ->
            val deleteIcon = R.drawable.ic_delete
            val checkIcon = R.drawable.ic_check
            val labelIcon = R.drawable.ic_label_no_fill
            val pencilIcon = R.drawable.ic_pencil

            if (boolean) {

                textInputLayout.setStartIconDrawable(deleteIcon)
                textInputLayout.setEndIconDrawable(checkIcon)
                textInputLayout.setEndIconOnClickListener {
                    adapterScope.launch {
                        val label = Label(
                            labelId = label.labelId,
                            labelName = textInputLayout.editText?.text.toString().trim()
                        )

                    }
                    textInputLayout.clearFocus()
                }
                textInputLayout.setStartIconOnClickListener {
                    adapterScope.launch {
                        noteDao.deleteLabel(label)
                    }
                    textInputLayout.clearFocus()
                }
            } else {
                textInputLayout.setStartIconDrawable(labelIcon)
                textInputLayout.setEndIconDrawable(pencilIcon)
                adapterScope.launch {
                    val label = Label(
                        labelId = label.labelId,
                        labelName = textInputLayout.editText?.text.toString().trim()
                    )
                    try {
                        Log.i("label_adapter", "update label: successfully updated")
                        noteDao.updateLabel(label)
                    } catch (e: Exception) {
                        Log.i("label_adapter", "update label: ${e.message}")
                    }
                }
                textInputLayout.setEndIconOnClickListener {
                }
                textInputLayout.setStartIconOnClickListener {
                }
            }
        }
    }
}