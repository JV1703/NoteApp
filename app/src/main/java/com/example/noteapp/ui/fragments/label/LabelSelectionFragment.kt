package com.example.noteapp.ui.fragments.label

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.noteapp.BaseApplication
import com.example.noteapp.adapter.recyclerview.labeladapter.LabelSelectAdapter
import com.example.noteapp.databinding.FragmentLabelSelectionBinding
import com.example.noteapp.ui.fragments.NoteViewModel
import com.example.noteapp.ui.fragments.NoteViewModelFactory

class LabelSelectionFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentLabelSelectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var labelSelectAdapter: LabelSelectAdapter

    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as BaseApplication).noteDatabase.noteDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        labelSelectAdapter = LabelSelectAdapter(noteViewModel)
        _binding = FragmentLabelSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setupRecyclerView()
        getLabels()
    }

    private fun setListeners() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.searchBar.setOnQueryTextListener(this)
    }

    private fun getLabels() {
        noteViewModel.allLabel.observe(viewLifecycleOwner) {
            labelSelectAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        binding.labelRv.adapter = labelSelectAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchLabel(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchLabel(newText)
        }
        return true
    }

    private fun searchLabel(labelSearchQuery: String) {
        val searchQuery = "%$labelSearchQuery%"

        noteViewModel.searchLabel(searchQuery).observe(viewLifecycleOwner) { list ->
            list?.let {
                labelSelectAdapter.submitList(it)
            }
        }
    }

}