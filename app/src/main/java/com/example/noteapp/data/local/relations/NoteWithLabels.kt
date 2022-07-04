package com.example.noteapp.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.noteapp.data.local.model.Label
import com.example.noteapp.data.local.model.Note

data class NoteWithLabels(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "labelId",
        associateBy = Junction(NoteLabelCrossRef::class)
    )
    val labels: List<Label>
)