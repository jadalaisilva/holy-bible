package com.jadalai.reinavalera1960.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "testament")
    val testament: String, // "OT" (Antiguo Testamento) or "NT" (Nuevo Testamento)
    @ColumnInfo(name = "order_index")
    val orderIndex: Int,
    @ColumnInfo(name = "chapters_count")
    val chaptersCount: Int
)
