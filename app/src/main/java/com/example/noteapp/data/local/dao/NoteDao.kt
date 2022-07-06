package com.example.noteapp.data.local.dao


import androidx.room.*
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.data.local.model.Note
import com.example.noteapp.data.local.relations.LabelWithNotes
import com.example.noteapp.data.local.relations.NoteLabelCrossRef
import com.example.noteapp.data.local.relations.NoteWithLabels
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertLabel(label: Label)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteLabelCrossRef(crossRef: NoteLabelCrossRef)

    @Update()
    fun updateNote(note: Note)

    @Update()
    fun updateLabel(label: Label)

    @Delete
    fun deleteNote(note: Note)

    @Delete
    fun deleteLabel(label: Label)

    @Query("SELECT * FROM Note ORDER BY timeStamp DESC")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE noteId = :noteId")
    fun getNoteById(noteId: Long): Flow<Note>

    @Query("SELECT * FROM Label")
    fun getLabels(): Flow<List<Label>>

    @Transaction
    @Query("SELECT * FROM Note where noteId = :noteId")
    fun getLabelsOfNote(noteId: Long): Flow<List<NoteWithLabels>>

    @Transaction
    @Query("SELECT * FROM Label where labelId = :labelId")
    fun getNotesOfLabel(labelId: Long): Flow<List<LabelWithNotes>>

}