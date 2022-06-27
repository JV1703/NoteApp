package com.example.noteapp.data.local.dao


import androidx.room.*
import com.example.noteapp.data.local.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update()
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM Note ORDER BY timeStamp DESC")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE id = :id ORDER BY timeStamp")
    fun getNoteById(id: Long): Flow<Note>


//    @Transaction
//    @Query("SELECT * FROM Note WHERE id = :noteId")
//    fun getNoteWithImages(noteId: Long): Flow<List<NoteWithImages>>

}