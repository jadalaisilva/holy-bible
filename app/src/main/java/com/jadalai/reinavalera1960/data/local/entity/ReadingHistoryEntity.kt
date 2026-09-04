package com.jadalai.reinavalera1960.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_history")
data class ReadingHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "book_id")
    val bookId: Int,
    @ColumnInfo(name = "book_name")
    val bookName: String,
    @ColumnInfo(name = "chapter_number")
    val chapterNumber: Int,
    @ColumnInfo(name = "timestamp_read")
    val timestampRead: Long = System.currentTimeMillis()
)
