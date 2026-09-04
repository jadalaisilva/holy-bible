package com.jadalai.reinavalera1960.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.jadalai.reinavalera1960.data.local.entity.ReadingHistoryEntity

@Dao
interface ReadingHistoryDao {
    @Query("SELECT * FROM reading_history ORDER BY timestamp_read DESC")
    fun getAllHistory(): Flow<List<ReadingHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: ReadingHistoryEntity)

    @Query("SELECT COUNT(*) > 0 FROM reading_history WHERE book_id = :bookId AND chapter_number = :chapterNumber")
    fun hasReadChapter(bookId: Int, chapterNumber: Int): Flow<Boolean>

    @Query("SELECT * FROM reading_history WHERE book_id = :bookId AND chapter_number = :chapterNumber LIMIT 1")
    suspend fun getReadChapter(bookId: Int, chapterNumber: Int): ReadingHistoryEntity?

    @Delete
    suspend fun deleteHistory(history: ReadingHistoryEntity)

    @Query("DELETE FROM reading_history")
    suspend fun clearAllHistory()
}
