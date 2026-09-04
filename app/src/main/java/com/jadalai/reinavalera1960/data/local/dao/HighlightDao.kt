package com.jadalai.reinavalera1960.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.jadalai.reinavalera1960.data.local.entity.HighlightEntity

@Dao
interface HighlightDao {
    @Query("SELECT * FROM highlights")
    fun getAllHighlights(): Flow<List<HighlightEntity>>

    @Query("SELECT * FROM highlights WHERE verse_id = :verseId LIMIT 1")
    suspend fun getHighlightByVerseId(verseId: Int): HighlightEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlight(highlight: HighlightEntity)

    @Query("DELETE FROM highlights WHERE verse_id = :verseId")
    suspend fun deleteHighlightByVerseId(verseId: Int)
}
