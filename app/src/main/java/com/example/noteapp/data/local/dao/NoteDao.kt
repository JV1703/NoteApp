package com.example.noteapp.data.local.dao


import androidx.room.*
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.data.local.model.Note
import com.example.noteapp.data.local.relations.LabelWithNotes
import com.example.noteapp.data.local.relations.NoteWithLabels
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabel(label: Label)

    @Update()
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM Note ORDER BY timeStamp DESC")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE noteId = :noteId ORDER BY timeStamp")
    fun getNoteById(noteId: Long): Flow<Note>

    @Transaction
    @Query("SELECT * FROM Note where noteId = :noteId")
    fun getNoteWithLabels(noteId: Long): Flow<NoteWithLabels>

    @Transaction
    @Query("SELECT * FROM Label where labelId = :labelId")
    fun getLabelWithNotes(labelId: Long): Flow<LabelWithNotes>


//    @Transaction
//    @Query("SELECT * FROM Note WHERE id = :noteId")
//    fun getNoteWithImages(noteId: Long): Flow<List<NoteWithImages>>

}