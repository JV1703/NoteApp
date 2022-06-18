package com.example.noteapp.adapter.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.local.model.Note
import com.example.noteapp.databinding.NoteViewHolderBinding
import com.example.noteapp.databinding.RvHeaderViewHolderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class DataItem {
    data class NoteItem(val note: Note) : DataItem()
    data class Header(val text: String) : DataItem()
}

class NotesAdapter : ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    val ITEM_VIEW_TYPE_HEADER = 0
    val ITEM_VIEW_TYPE_ITEM = 1

    class NoteViewHolder(val binding: NoteViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.note = note
            binding.noteRoot.setBackgroundColor(note.bgColor)
        }
    }

    class HeaderViewHolder(val binding: RvHeaderViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.tv.text = text
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.NoteItem -> ITEM_VIEW_TYPE_ITEM
            else -> Log.e("sleep_night_adapter", "#N/A View Holder")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder(
                RvHeaderViewHolderBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            ITEM_VIEW_TYPE_ITEM -> NoteViewHolder(
                NoteViewHolderBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val header = getItem(position) as DataItem.Header
                holder.bind(header.text)
            }
            is NoteViewHolder -> {
                val noteItem = getItem(position) as DataItem.NoteItem
                holder.bind(noteItem.note)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    fun pinnedSorting(list: List<Note>) {
        adapterScope.launch {
            val pinned = list.filter { it.pinned }
            val notPinned = list.filterNot { it.pinned }
            val finalList: List<DataItem> =
                if (pinned.isNotEmpty() && notPinned.isNotEmpty()) {
                    listOf(DataItem.Header("Pinned")) + pinned.map { DataItem.NoteItem(it) } + listOf(
                        DataItem.Header("Others")
                    ) + notPinned.map {
                        DataItem.NoteItem(it)
                    }
                } else if (pinned.isNotEmpty()) {
                    listOf(DataItem.Header("Pinned")) + pinned.map { DataItem.NoteItem(it) }
                } else {
                    notPinned.map { DataItem.NoteItem(it) }
                }
            withContext(Dispatchers.Main) {
                submitList(finalList)
            }
        }
    }
}