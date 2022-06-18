package com.example.noteapp.data.local.usecase

import com.example.noteapp.data.local.dao.NoteDao
import com.example.noteapp.data.local.model.Note
import kotlinx.coroutines.flow.Flow

class Local(val noteDao: NoteDao) {

    fun getNotes(): Flow<List<Note>> = noteDao.getNotes()

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

}