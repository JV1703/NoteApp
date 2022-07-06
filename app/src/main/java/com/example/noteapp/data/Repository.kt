package com.example.noteapp.data

import com.example.noteapp.data.local.dao.NoteDao
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.data.local.model.Note
import com.example.noteapp.data.local.relations.LabelWithNotes
import com.example.noteapp.data.local.relations.NoteLabelCrossRef
import com.example.noteapp.data.local.relations.NoteWithLabels
import kotlinx.coroutines.flow.Flow

class Repository(val noteDao: NoteDao) {

    fun getNotes(): Flow<List<Note>> = noteDao.getNotes()
    fun getNoteById(noteId: Long): Flow<Note> = noteDao.getNoteById(noteId)
    fun getLabels(): Flow<List<Label>> = noteDao.getLabels()

    fun getLabelsOfNote(noteId: Long): Flow<List<NoteWithLabels>> = noteDao.getLabelsOfNote(noteId)
    fun getNotesOfLabel(labelId: Long): Flow<List<LabelWithNotes>> =
        noteDao.getNotesOfLabel(labelId)
//    fun getNoteWithImages(id: Long): Flow<List<NoteWithImages>> = noteDao.getNoteWithImages(id)

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun insertLabel(label: Label){
        noteDao.insertLabel(label)
    }

    suspend fun insertNoteLabelCrossReference(crossRef: NoteLabelCrossRef){
        noteDao.insertNoteLabelCrossRef(crossRef)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun updateLabel(label: Label){
        noteDao.updateLabel(label)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun deleteLabel(label: Label) {
        noteDao.deleteLabel(label)
    }

}