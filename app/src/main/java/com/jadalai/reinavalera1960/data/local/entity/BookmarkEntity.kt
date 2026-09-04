package com.jadalai.reinavalera1960.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookmarks",
    indices = [Index(value = ["verse_id"])]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "verse_id")
    val verseId: Long,
    @ColumnInfo(name = "book_name")
    val bookName: String,
    @ColumnInfo(name = "chapter_number")
    val chapterNumber: Int,
    @ColumnInfo(name = "verse_number")
    val verseNumber: Int,
    @ColumnInfo(name = "verse_end_number")
    val verseEndNumber: Int = verseNumber,
    @ColumnInfo(name = "verse_text")
    val verseText: String,
    @ColumnInfo(name = "custom_note")
    val customNote: String? = null,
    @ColumnInfo(name = "timestamp_added")
    val timestampAdded: Long = System.currentTimeMillis()
)
