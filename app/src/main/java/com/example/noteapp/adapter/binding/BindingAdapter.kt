package com.example.noteapp.adapter.binding

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.noteapp.data.local.model.Note
import com.example.noteapp.ui.fragments.MainFragmentDirections
import com.example.noteapp.utils.dateToStringConverter
import java.util.*

@BindingAdapter("show_note")
fun showNote(view: CardView, note: Note?) {
    view.setOnClickListener {
        if (note !=null){
            view.findNavController().navigate(MainFragmentDirections.actionMainFragmentToUpdateNoteFragment(note))
        }
    }
}

@BindingAdapter("time_stamp")
fun timeStamp(tv: TextView, date: Date){
    tv.text = dateToStringConverter(date)
}