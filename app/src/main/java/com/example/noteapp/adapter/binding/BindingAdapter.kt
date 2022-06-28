package com.example.noteapp.adapter.binding

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.local.model.Note
import com.example.noteapp.ui.fragments.MainFragmentDirections
import com.example.noteapp.utils.dateToStringConverter
import com.google.android.material.textfield.TextInputLayout
import java.util.*

@BindingAdapter("show_note")
fun showNote(view: CardView, note: Note?) {
    view.setOnClickListener {
        if (note != null) {
            view.findNavController()
                .navigate(MainFragmentDirections.actionMainFragmentToUpdateNoteFragment(note))
        }
    }
}

@BindingAdapter("time_stamp")
fun timeStamp(tv: TextView, date: Date) {
    tv.text = dateToStringConverter(date)
}

//@BindingAdapter("labelVhIconFocus")
//fun labelVhIconFocus(textInputLayout: TextInputLayout) {
//    textInputLayout.setOnFocusChangeListener { view, boolean ->
//        val deleteIcon = R.drawable.ic_delete
//        val checkIcon = R.drawable.ic_check
//        val labelIcon = R.drawable.ic_label_no_fill
//        val pencilIcon = R.drawable.ic_pencil
//
//        if (boolean) {
//            textInputLayout.setStartIconDrawable(deleteIcon)
//            textInputLayout.setEndIconDrawable(checkIcon)
//
//            textInputLayout.addOnEditTextAttachedListener {
//                if (textInputLayout.editText?.text?.isEmpty() == true) {
//                    textInputLayout.error = "Enter a label name"
//                } else {
//                    textInputLayout.error = null
//                }
//            }
//        } else {
//            textInputLayout.setStartIconDrawable(labelIcon)
//            textInputLayout.setEndIconDrawable(pencilIcon)
//        }
//    }
//}
//
//@BindingAdapter("labelVhIconClickListener")
//fun labelVhIconClickListener(textInputLayout: TextInputLayout) {
//    if (textInputLayout.endIconDrawable == textInputLayout.context.getDrawable(R.drawable.ic_check)) {
//        textInputLayout.setEndIconOnClickListener { }
//    }
//}