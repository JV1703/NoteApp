package com.example.noteapp.ui.fragments.label

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.noteapp.BaseApplication
import com.example.noteapp.R
import com.example.noteapp.adapter.recyclerview.labeladapter.LabelAdapter
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.databinding.FragmentLabelBinding
import com.example.noteapp.ui.fragments.NoteViewModel
import com.example.noteapp.ui.fragments.NoteViewModelFactory
import com.example.noteapp.utils.showKeyboard

class LabelFragment : Fragment() {

    private var _binding: FragmentLabelBinding? = null
    private val binding get() = _binding!!

    private lateinit var labelAdapter: LabelAdapter
    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as BaseApplication).noteDatabase.noteDao())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        labelAdapter = LabelAdapter(requireActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLabelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLabels()
        setupRecyclerView()
        setListeners()
    }

    private fun setListeners() {
        textFieldFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun textFieldFocus() {
        binding.addLabelTextField.editText?.setOnFocusChangeListener { view, boolean ->
            if (boolean) {
                binding.addLabelTextField.endIconDrawable =
                    requireContext().getDrawable(R.drawable.ic_check)
                binding.addLabelTextField.startIconDrawable =
                    requireContext().getDrawable(R.drawable.ic_clear)
                saveLabel()
            } else {
                binding.addLabelTextField.endIconDrawable = null
                binding.addLabelTextField.startIconDrawable =
                    requireContext().getDrawable(R.drawable.ic_add_note)
                binding.addLabelTextField.setEndIconOnClickListener {
                    binding.addLabelTextField.editText?.requestFocus()
                    showKeyboard(binding.addLabelTextField.editText!!, requireContext())
                }
            }
        }
    }

    private fun saveLabel() {
        binding.addLabelTextField.setEndIconOnClickListener {
            val label = Label(
                labelName = binding.addLabelTextField.editText?.text.toString().trim()
            )
            noteViewModel.saveLabel(label)
            binding.addLabelTextField.editText?.text = null
            binding.addLabelTextField.clearFocus()
        }
    }

    private fun setupRecyclerView() {
        binding.labelRv.adapter = labelAdapter
        binding.labelRv.itemAnimator = null
    }

    private fun getLabels() {
        noteViewModel.allLabel.observe(viewLifecycleOwner) {
            labelAdapter.submitList(it)
        }
    }
}