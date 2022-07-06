package com.example.noteapp.data.local.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.util.*

class TypeConverter {

    val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun fromStringToArrayList(value: String): ArrayList<Label> {
        val listType = object : TypeToken<ArrayList<Label>>() {}.type
        Log.e("TypeConverter", "String to Label -> Conversion is made")
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayListToString(list: ArrayList<Label>): String {
        return gson.toJson(list)
    }

}