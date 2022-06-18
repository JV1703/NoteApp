package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.BaseApplication
import com.example.noteapp.R
import com.example.noteapp.adapter.recyclerview.NotesAdapter
import com.example.noteapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesAdapter: NotesAdapter
    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as BaseApplication).noteDatabase.noteDao())
    }

    private var isGridLayoutManager = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesAdapter = NotesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setupRecyclerView()

        noteViewModel.allNotes.observe(viewLifecycleOwner) {
            notesAdapter.pinnedSorting(it)
        }
    }

    private fun setListeners() {
        binding.newNote.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToAddNoteFragment()
            findNavController().navigate(action)
        }

        binding.viewType.setOnClickListener { v ->
            isGridLayoutManager = !isGridLayoutManager
            chooseRecyclerViewLayout()
            chooseLayoutIcon()
        }
    }

    private fun setupRecyclerView() {
        binding.notesRecyclerView.adapter = notesAdapter
        chooseRecyclerViewLayout()
    }

    private fun chooseRecyclerViewLayout() {
        if (isGridLayoutManager) {
            val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            binding.notesRecyclerView.layoutManager = layoutManager
        } else {
            binding.notesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun chooseLayoutIcon() {
        if (isGridLayoutManager) {
            binding.viewType.setImageResource(R.drawable.ic_grid_view)
        } else {
            binding.viewType.setImageResource(R.drawable.ic_list_view)
        }

//        notesAdapter.notifyItemRangeChanged(0, notesAdapter.itemCount)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}