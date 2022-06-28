package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.BaseApplication
import com.example.noteapp.R
import com.example.noteapp.adapter.recyclerview.NotesAdapter
import com.example.noteapp.adapter.recyclerview.noteadapter.NoteAdapter
import com.example.noteapp.adapter.recyclerview.noteadapter.PinnedItemAdapter
import com.example.noteapp.databinding.FragmentMainBinding
import com.example.noteapp.ui.animation.startAnimation
import com.google.android.material.navigation.NavigationView

class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesAdapter: NotesAdapter
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var pinnedItemAdapter: PinnedItemAdapter
    private lateinit var concatAdapter: ConcatAdapter
    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as BaseApplication).noteDatabase.noteDao())
    }

    private var isGridLayoutManager = true
    private var pinToggle = false
    private var sortByTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesAdapter = NotesAdapter()
        noteAdapter = NoteAdapter()
        pinnedItemAdapter = PinnedItemAdapter()
        concatAdapter = ConcatAdapter(pinnedItemAdapter, noteAdapter)
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
        binding.navView.setNavigationItemSelectedListener(this)

        setListeners()
        setupRecyclerView()
        drawerToggle()

        noteViewModel.allNotes.observe(viewLifecycleOwner) { data ->

//            noteAdapter.pinnedSorting(data, isGridLayoutManager)
//            pinnedItemAdapter.pinnedSorting(data, isGridLayoutManager)

            notesAdapter.submitList(data)

            binding.viewType.setOnClickListener {
                isGridLayoutManager = !isGridLayoutManager
//                noteAdapter.pinnedSorting(data, isGridLayoutManager)
//                pinnedItemAdapter.pinnedSorting(data, isGridLayoutManager)
                chooseRecyclerViewLayout()
                chooseLayoutIcon()
            }

            binding.pin.setOnClickListener {
                pinToggle()
                val prioritizePin = data.sortedBy { it.pinned }
                if (pinToggle) {
                    notesAdapter.submitList(prioritizePin)
                } else {
                    notesAdapter.submitList(data)
                }
            }


            binding.sortTime.setOnClickListener {
                sortByTimeToggle()
                val toLatest = data.sortedBy { it.timeStamp }
                if (sortByTime) {
                    notesAdapter.submitList(data)
                } else {
                    notesAdapter.submitList(toLatest)
                }
            }
        }
    }

    private fun setListeners() {
        binding.newNote.setOnClickListener {

            val animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.circle_expand_animation)
                    .apply {
                        duration = 1000
                        interpolator = AccelerateInterpolator()
                    }

            binding.newNote.visibility = View.GONE
            binding.circle.visibility = View.VISIBLE

            binding.circle.startAnimation(animation) {
                val action = MainFragmentDirections.actionMainFragmentToAddNoteFragment()
                findNavController().navigate(action)
            }
            val action = MainFragmentDirections.actionMainFragmentToAddNoteFragment()
            findNavController().navigate(action)
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
    }

    private fun pinToggle() {
        if (pinToggle) {
            binding.pin.setImageResource(R.drawable.ic_pin_fill)
        } else {
            binding.pin.setImageResource(R.drawable.ic_pin_no_fill)
        }
        pinToggle = !pinToggle
    }

    private fun sortByTimeToggle() {
        if (sortByTime) {
            binding.sortTime.setImageResource(R.drawable.ic_anticlockwise)
        } else {
            binding.sortTime.setImageResource(R.drawable.ic_clockwise)
        }
        sortByTime = !sortByTime
    }

    private fun drawerToggle() {
        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notes -> {
                Toast.makeText(requireContext(), "notes", Toast.LENGTH_SHORT).show()
            }
            R.id.reminder -> {
                Toast.makeText(requireContext(), "reminder", Toast.LENGTH_SHORT).show()
            }
            R.id.labels -> {
                val action = MainFragmentDirections.actionMainFragmentToLabelFragment()
                findNavController().navigate(action)
            }
            R.id.delete -> {
                Toast.makeText(requireContext(), "delete", Toast.LENGTH_SHORT).show()
            }
            R.id.settings -> {
                Toast.makeText(requireContext(), "settings", Toast.LENGTH_SHORT).show()
            }
            R.id.help -> {
                Toast.makeText(requireContext(), "help", Toast.LENGTH_SHORT).show()
            }
        }
        binding.drawerLayout.close()
        return true
    }

}