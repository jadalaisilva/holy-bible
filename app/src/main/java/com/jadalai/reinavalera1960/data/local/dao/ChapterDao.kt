package com.jadalai.reinavalera1960.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.jadalai.reinavalera1960.data.local.entity.ChapterEntity

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters WHERE book_id = :bookId ORDER BY chapter_number ASC")
    fun getChaptersForBook(bookId: Int): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE id = :chapterId LIMIT 1")
    suspend fun getChapterById(chapterId: Int): ChapterEntity?

    @Query("SELECT * FROM chapters WHERE book_id = :bookId AND chapter_number = :chapterNumber LIMIT 1")
    suspend fun getChapterByBookAndNumber(bookId: Int, chapterNumber: Int): ChapterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)
}
