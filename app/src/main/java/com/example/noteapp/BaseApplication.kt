package com.example.noteapp

import android.app.Application
import com.example.noteapp.data.local.NoteDatabase

class BaseApplication : Application() {

    val noteDatabase: NoteDatabase by lazy { NoteDatabase.getDatabase(this) }

}