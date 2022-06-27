package com.example.noteapp.ui.fragments.notefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp.BaseApplication
import com.example.noteapp.R
import com.example.noteapp.data.local.model.Note
import com.example.noteapp.databinding.FragmentUpdateNoteBinding
import com.example.noteapp.ui.fragments.NoteViewModel
import com.example.noteapp.ui.fragments.NoteViewModelFactory
import com.example.noteapp.utils.makeToast
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import java.util.*

class UpdateNoteFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!
    private val navArgs: UpdateNoteFragmentArgs by navArgs()
    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as BaseApplication).noteDatabase.noteDao())
    }
    private var bgColor: Int = -1
    private var pinStatus: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pinStatus = navArgs.note.pinned
        bgColor = navArgs.note.bgColor
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNote()
        setListeners()
        binding.note = navArgs.note
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    private fun setListeners() {
        binding.save.setOnClickListener {
            saveNote()
        }

        binding.bgSelector.setOnClickListener {
            colorPicker()
        }

        binding.pin.setOnClickListener {
            setPinStatus()
            Toast.makeText(requireContext(), "pin status: $pinStatus", Toast.LENGTH_SHORT).show()
        }

        binding.back.setOnClickListener {
            val action = UpdateNoteFragmentDirections.actionUpdateNoteFragmentToMainFragment()
            findNavController().navigate(action)
        }

        binding.vertMenu.setOnClickListener {
            showPopup(it)
        }
    }

    private fun loadNote() {
        binding.noteRoot.setBackgroundColor(navArgs.note.bgColor)

        if (pinStatus) {
            binding.pin.setImageResource(R.drawable.ic_pin_fill)
        } else {
            binding.pin.setImageResource(R.drawable.ic_pin_no_fill)
        }
    }

    private fun saveNote() {
        val note = Note(
            id = navArgs.note.id,
            title = binding.noteTitle.text.toString(),
            text = binding.noteBody.text.toString(),
            timeStamp = Date(),
            bgColor = bgColor,
            pinned = pinStatus
        )
        noteViewModel.saveNote(note)
    }

    private fun colorPicker() {

        MaterialColorPickerDialog
            .Builder(requireActivity())
            .setColorShape(ColorShape.CIRCLE)
            .setColorSwatch(ColorSwatch._300)
            .setColorRes(resources.getIntArray(R.array.bg_color))
            .setColorListener(object : ColorListener {
                override fun onColorSelected(color: Int, colorHex: String) {
                    bgColor = color
                    binding.noteRoot.setBackgroundColor(color)
                }
            })
            .showBottomSheet(childFragmentManager)
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(requireContext(), v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.popup_menu, popup.menu)
        popup.setForceShowIcon(true)
        popup.show()

        popup.setOnMenuItemClickListener(this)
    }

    private fun deleteNote(note: Note) {
        try {
            noteViewModel.deleteNote(note)
            val action = UpdateNoteFragmentDirections.actionUpdateNoteFragmentToMainFragment()
            findNavController().navigate(action)
        } catch (e: Exception) {
            makeToast("Unable to delete note")
            Log.d("note_view_model", "deleteNote: ${e.message}")
        }
    }

    private fun setPinStatus() {
        pinStatus = !pinStatus
        if (pinStatus) {
            binding.pin.setImageResource(R.drawable.ic_pin_fill)
        } else {
            binding.pin.setImageResource(R.drawable.ic_pin_no_fill)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete -> deleteNote(navArgs.note)
            R.id.labels -> Toast.makeText(requireContext(), "Labels", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}