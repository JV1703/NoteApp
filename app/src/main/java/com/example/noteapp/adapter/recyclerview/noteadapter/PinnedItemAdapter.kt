package com.example.noteapp.adapter.recyclerview.noteadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.adapter.recyclerview.NotesAdapter
import com.example.noteapp.data.local.model.Note
import com.example.noteapp.databinding.NoteViewHolderBinding
import com.example.noteapp.databinding.RvHeaderViewHolderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//sealed class DataItem {
//    data class NoteItem(val note: Note) : DataItem()
//    data class Header(val text: String) : DataItem()
//}

class PinnedItemAdapter : ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback) {

    val ITEM_VIEW_TYPE_HEADER = 0
    val ITEM_VIEW_TYPE_ITEM = 1

    private val adapterScope = CoroutineScope(Dispatchers.Default)

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
            else -> throw ClassCastException("Unknown viewType $viewType")
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

    fun pinnedSorting(list: List<Note>, isGridLayout: Boolean) {
        adapterScope.launch {
            val pinned = list.filter { it.pinned }
            val finalList =
                if (isGridLayout) {
                    listOf(DataItem.Header("Pinned")) + listOf(DataItem.Header("")) + pinned.map { DataItem.NoteItem(it) }
                } else {
                    listOf(DataItem.Header("Pinned")) + pinned.map { DataItem.NoteItem(it) }
                }

            withContext(Dispatchers.Main){
                if (pinned.isEmpty()) {
                    submitList(pinned.map { DataItem.NoteItem(it) })
                } else {
                    submitList(finalList)
                }
            }
        }
    }
}