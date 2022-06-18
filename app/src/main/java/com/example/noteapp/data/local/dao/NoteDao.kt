package com.example.noteapp.data.local.dao


import androidx.room.*
import com.example.noteapp.data.local.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertImage(image: MediaStore.Images)

    @Update()
    fun updateNote(note: Note)

    @Query("SELECT * FROM Note ORDER BY timeStamp DESC")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE id = :id ORDER BY timeStamp")
    fun getNoteById(id: Long): Flow<Note>

//    @Transaction
//    @Query("SELECT * FROM Note WHERE id = :noteId")
//    fun getNoteWithImages(noteId: Long): Flow<List<NoteWithImages>>

}