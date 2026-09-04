package com.jadalai.reinavalera1960.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorites",
    foreignKeys = [
        ForeignKey(
            entity = VerseEntity::class,
            parentColumns = ["id"],
            childColumns = ["verse_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["verse_id"])]
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "verse_id")
    val verseId: Int,
    @ColumnInfo(name = "book_name")
    val bookName: String,
    @ColumnInfo(name = "chapter_number")
    val chapterNumber: Int,
    @ColumnInfo(name = "verse_number")
    val verseNumber: Int,
    @ColumnInfo(name = "verse_text")
    val verseText: String,
    @ColumnInfo(name = "timestamp_added")
    val timestampAdded: Long = System.currentTimeMillis()
)
