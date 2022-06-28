package com.example.noteapp.ui.fragments

import android.util.Log
import androidx.lifecycle.*
import com.example.noteapp.data.Repository
import com.example.noteapp.data.local.dao.NoteDao
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.data.local.model.Note
import com.example.noteapp.utils.addNewItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {

    private val repository = Repository(noteDao)

    val allNotes = repository.getNotes().asLiveData()
    val allLabel = repository.getLabels().asLiveData()
    private var _pinned = false
    val pinned get() = _pinned

    private val _imgList = MutableLiveData<MutableList<ByteArray>>()
    val imgList: LiveData<MutableList<ByteArray>> get() = _imgList

    fun saveNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertNote(note)
            } catch (e: Exception) {
                Log.d("note_view_model", "saveNote: ${e.message}")
            }
        }
    }

    fun saveLabel(label: Label) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertLabel(label)
            } catch (e: Exception) {
                Log.d("note_view_model", "saveLabel: ${e.message}")
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun addImage(image: ByteArray) {
        _imgList.addNewItem(image)
        Log.i("image_list", "${_imgList.value?.isEmpty()}")
    }

    fun setPinned() {
        _pinned = !_pinned
    }
}

class NoteViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}