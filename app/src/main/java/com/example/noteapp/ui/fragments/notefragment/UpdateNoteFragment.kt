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
import com.example.noteapp.adapter.recyclerview.labeladapter.LabelSelectAdapter
import com.example.noteapp.adapter.recyclerview.noteadapter.MiniLabelAdapter
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.data.local.model.Note
import com.example.noteapp.databinding.FragmentUpdateNoteBinding
import com.example.noteapp.ui.fragments.NoteViewModel
import com.example.noteapp.ui.fragments.NoteViewModelFactory
import com.example.noteapp.utils.makeToast
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
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

    private lateinit var miniLabelAdapter: MiniLabelAdapter
    private lateinit var labelSelectAdapter: LabelSelectAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pinStatus = navArgs.note.pinned
        bgColor = navArgs.note.bgColor
        miniLabelAdapter = MiniLabelAdapter()
        labelSelectAdapter = LabelSelectAdapter(noteViewModel)
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        noteViewModel.saveSelectedLabels(navArgs.note.label)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNote()
        setListeners()
        noteViewModel.allLabel.observe(viewLifecycleOwner) { labels ->
            setupMiniLabelAdapter(dbLabel = labels.toMutableSet(), navArgLabel = navArgs.note.label)
        }
        binding.note = navArgs.note
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        noteViewModel.clearSelectedLabels()
        Log.i("update_note_fragment", "onDestroy is triggered")
    }

    private fun setListeners() {
        binding.save.setOnClickListener {
            saveNote()
            val action = UpdateNoteFragmentDirections.actionUpdateNoteFragmentToMainFragment()
            findNavController().navigate(action)
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
            noteId = navArgs.note.noteId,
            title = binding.noteTitle.text.toString(),
            text = binding.noteBody.text.toString(),
            timeStamp = Date(),
            bgColor = bgColor,
            pinned = pinStatus,
            label = noteViewModel.selectedLabels
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

    private fun setupMiniLabelAdapter(
        dbLabel: MutableSet<Label>,
        navArgLabel: MutableSet<Label>
    ) {
        binding.labelRv.adapter = miniLabelAdapter
        val layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.WRAP)
        binding.labelRv.layoutManager = layoutManager
        if (navArgs.note.label.isEmpty()) {
            binding.labelRv.visibility = View.GONE
        } else {
            miniLabelAdapter.submitList(labelCheck(dbLabel = dbLabel, navArgLabel = navArgLabel))
            binding.labelRv.visibility = View.VISIBLE
        }
    }

    private fun labelCheck(
        dbLabel: MutableSet<Label>,
        navArgLabel: MutableSet<Label>
    ): List<Label> {
        var result: MutableSet<Label> = mutableSetOf()
        if (dbLabel == navArgLabel) {
            result = navArgLabel
        } else {
            navArgLabel.forEach { label ->
                if (dbLabel.contains(label)) {
                    result.add(label)
                }
            }
        }
        noteViewModel.saveSelectedLabels(result)
        return result.toList()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete -> deleteNote(navArgs.note)
            R.id.labels -> {
                val action =
                    UpdateNoteFragmentDirections.actionUpdateNoteFragmentToLabelSelectionFragment()
                findNavController().navigate(action)
            }
        }
        return true
    }
}