package com.example.noteapp.data.local.relations

import androidx.room.Entity

@Entity(primaryKeys = ["noteId", "labelId"])
class NoteWithLabelCrossRef(
    val noteId: Long,
    val labelId: Long
)