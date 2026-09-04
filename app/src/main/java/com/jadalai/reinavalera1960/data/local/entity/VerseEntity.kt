package com.jadalai.reinavalera1960.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "verses",
    foreignKeys = [
        ForeignKey(
            entity = ChapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapter_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["chapter_id"]),
        Index(value = ["content_text"]),
        Index(value = ["translation"])
    ]
)
data class VerseEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "chapter_id")
    val chapterId: Int,
    @ColumnInfo(name = "book_id")
    val bookId: Int,
    @ColumnInfo(name = "verse_number")
    val verseNumber: Int,
    @ColumnInfo(name = "content_text")
    val contentText: String,
    @ColumnInfo(name = "translation", defaultValue = "'rvr1960'")
    val translation: String = "rvr1960",
    @ColumnInfo(name = "audio_url")
    val audioUrl: String? = null
)
