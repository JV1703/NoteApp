package com.example.noteapp.data

import com.example.noteapp.data.local.dao.NoteDao
import com.example.noteapp.data.local.model.Note
//import com.example.noteapp.data.local.relations.NoteWithImages
import kotlinx.coroutines.flow.Flow

class Repository(val noteDao: NoteDao) {

    fun getNotes(): Flow<List<Note>> = noteDao.getNotes()
    fun getNoteById(id: Long): Flow<Note> = noteDao.getNoteById(id)
//    fun getNoteWithImages(id: Long): Flow<List<NoteWithImages>> = noteDao.getNoteWithImages(id)

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: Note){
        noteDao.updateNote(note)
    }

}