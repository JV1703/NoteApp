package com.example.noteapp.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.data.local.model.Note

data class LabelWithNotes(
    @Embedded val label: Label,
    @Relation(
        parentColumn = "labelId",
        entityColumn = "noteId",
        associateBy = Junction(NoteWithLabelCrossRef::class)
    )
    val notes: List<Note>
)