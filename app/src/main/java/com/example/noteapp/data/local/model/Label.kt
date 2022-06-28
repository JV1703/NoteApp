package com.example.noteapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Label(
    @PrimaryKey(autoGenerate = true)
    val labelId: Long = 0,
    val labelName: String
)