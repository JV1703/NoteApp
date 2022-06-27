package com.example.noteapp.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long = 0,
    val title: String,
    val text: String,
    val timeStamp: Date?,
    val reminder: Date? = null,
    val pinned: Boolean,
    val label: String? = null,
    var bgColor: Int
) : Parcelable

data class Images(var image: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Images

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}