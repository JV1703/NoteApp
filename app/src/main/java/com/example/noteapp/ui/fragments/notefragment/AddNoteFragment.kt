package com.example.noteapp.ui.fragments.notefragment

import android.graphics.Color
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
import com.example.noteapp.BaseApplication
import com.example.noteapp.R
import com.example.noteapp.adapter.recyclerview.ImageAdapter
import com.example.noteapp.data.local.model.Note
import com.example.noteapp.databinding.FragmentAddNoteBinding
import com.example.noteapp.ui.fragments.NoteViewModel
import com.example.noteapp.ui.fragments.NoteViewModelFactory
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*

class AddNoteFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private var bgColor = Color.parseColor("#FFFFFF")
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private var bottomSheetStatus = false
    private var imageAdapter = ImageAdapter()
    private var pinStatus = false

    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as BaseApplication).noteDatabase.noteDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.bottomSheet)

        noteViewModel.imgList.observe(viewLifecycleOwner) { image ->
            if (image.isNotEmpty()) {
                binding.imageRv.visibility = View.VISIBLE
                setupImgAdapter(image)
            }

            binding.back.setOnClickListener {
                Log.i("image_list", "isEmpty: ${image.isEmpty()} size: ${image.size}")
            }
        }
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

        binding.add.setOnClickListener {
            val action = AddNoteFragmentDirections.actionAddNoteFragmentToNoteBottomSheetFragment()
            findNavController().navigate(action)
        }

        binding.pin.setOnClickListener {
            setPinStatus()
        }

        binding.back.setOnClickListener {
            val action = AddNoteFragmentDirections.actionAddNoteFragmentToMainFragment()
            findNavController().navigate(action)
        }

        binding.vertMenu.setOnClickListener {
            showPopup(it)
        }
    }

    private fun saveNote() {
        val note = Note(
            title = binding.noteTitle.text.toString(),
            text = binding.noteBody.text.toString(),
            timeStamp = Date(),
            bgColor = bgColor,
            pinned = pinStatus
        )
        noteViewModel.saveNote(note)
        Toast.makeText(requireContext(), "note saved", Toast.LENGTH_SHORT).show()
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

    private fun setupImgAdapter(images: MutableList<ByteArray>) {
        imageAdapter = ImageAdapter()
        imageAdapter.submitList(images)
        binding.imageRv.adapter = imageAdapter
    }

    private fun setPinStatus() {
        pinStatus = !pinStatus
        if (pinStatus) {
            binding.pin.setImageResource(R.drawable.ic_pin_fill)
        } else {
            binding.pin.setImageResource(R.drawable.ic_pin_no_fill)
        }
        Toast.makeText(requireContext(), "pin status: ${pinStatus}", Toast.LENGTH_SHORT).show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete -> Toast.makeText(requireContext(), "Delete", Toast.LENGTH_SHORT).show()
            R.id.labels -> Toast.makeText(requireContext(), "Labels", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}