package com.jadalai.reinavalera1960.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "highlights",
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
data class HighlightEntity(
    @PrimaryKey
    @ColumnInfo(name = "verse_id")
    val verseId: Int,
    @ColumnInfo(name = "color_hex")
    val colorHex: String, // e.g. "#FFF59D" (Yellow), "#A5D6A7" (Green), "#90CAF9" (Blue), "#FFCCBC" (Orange), "#CE93D8" (Purple)
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)
