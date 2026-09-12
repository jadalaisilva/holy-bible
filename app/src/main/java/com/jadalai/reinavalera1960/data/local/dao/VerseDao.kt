package com.jadalai.reinavalera1960.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.jadalai.reinavalera1960.data.local.entity.VerseEntity

data class VerseWithDetails(
    val id: Long,
    val chapter_id: Int,
    val book_id: Int,
    val verse_number: Int,
    val content_text: String,
    val translation: String,
    val audio_url: String?,
    val book_name: String,
    val chapter_number: Int,
    val highlight_color: String?,
    val is_bookmarked: Boolean,
    val is_favorite: Boolean
)

@Dao
interface VerseDao {
    @Query("""
        SELECT v.*, b.name AS book_name, c.chapter_number AS chapter_number,
               h.color_hex AS highlight_color,
               (CASE WHEN bm.id IS NOT NULL THEN 1 ELSE 0 END) AS is_bookmarked,
               (CASE WHEN fav.id IS NOT NULL THEN 1 ELSE 0 END) AS is_favorite
        FROM verses v
        INNER JOIN chapters c ON v.chapter_id = c.id
        INNER JOIN books b ON v.book_id = b.id
        LEFT JOIN highlights h ON v.id = h.verse_id
        LEFT JOIN bookmarks bm ON v.id = bm.verse_id
        LEFT JOIN favorites fav ON v.id = fav.verse_id
        WHERE v.chapter_id = :chapterId AND v.translation = :translation
        ORDER BY v.verse_number ASC
    """)
    fun getVersesWithDetailsForChapter(chapterId: Int, translation: String = "rvr1960"): Flow<List<VerseWithDetails>>

    @Query("""
        SELECT v.*, b.name AS book_name, c.chapter_number AS chapter_number,
               h.color_hex AS highlight_color,
               (CASE WHEN bm.id IS NOT NULL THEN 1 ELSE 0 END) AS is_bookmarked,
               (CASE WHEN fav.id IS NOT NULL THEN 1 ELSE 0 END) AS is_favorite
        FROM verses v
        INNER JOIN chapters c ON v.chapter_id = c.id
        INNER JOIN books b ON v.book_id = b.id
        LEFT JOIN highlights h ON v.id = h.verse_id
        LEFT JOIN bookmarks bm ON v.id = bm.verse_id
        LEFT JOIN favorites fav ON v.id = fav.verse_id
        WHERE v.book_id = :bookId AND c.chapter_number = :chapterNumber 
          AND v.verse_number >= :startVerse AND v.verse_number <= :endVerse 
          AND v.translation = :translation
        ORDER BY v.verse_number ASC
    """)
    fun getVersesWithDetailsForRange(
        bookId: Int,
        chapterNumber: Int,
        startVerse: Int,
        endVerse: Int,
        translation: String = "rvr1960"
    ): Flow<List<VerseWithDetails>>

    @Query("SELECT * FROM verses WHERE chapter_id = :chapterId AND translation = :translation ORDER BY verse_number ASC")
    fun getVersesForChapter(chapterId: Int, translation: String = "rvr1960"): Flow<List<VerseEntity>>

    @Query("SELECT * FROM verses WHERE id = :verseId LIMIT 1")
    suspend fun getVerseById(verseId: Long): VerseEntity?

    @Query("""
        SELECT v.*, b.name AS book_name, c.chapter_number AS chapter_number,
               h.color_hex AS highlight_color,
               (CASE WHEN bm.id IS NOT NULL THEN 1 ELSE 0 END) AS is_bookmarked,
               (CASE WHEN fav.id IS NOT NULL THEN 1 ELSE 0 END) AS is_favorite
        FROM verses v
        INNER JOIN chapters c ON v.chapter_id = c.id
        INNER JOIN books b ON v.book_id = b.id
        LEFT JOIN highlights h ON v.id = h.verse_id
        LEFT JOIN bookmarks bm ON v.id = bm.verse_id
        LEFT JOIN favorites fav ON v.id = fav.verse_id
        WHERE v.content_text LIKE '%' || :query || '%' AND v.translation = :translation
        ORDER BY b.order_index ASC, c.chapter_number ASC, v.verse_number ASC
    """)
    suspend fun searchVerses(query: String, translation: String = "rvr1960"): List<VerseWithDetails>

    @Query("""
        SELECT v.*, b.name AS book_name, c.chapter_number AS chapter_number,
               h.color_hex AS highlight_color,
               (CASE WHEN bm.id IS NOT NULL THEN 1 ELSE 0 END) AS is_bookmarked,
               (CASE WHEN fav.id IS NOT NULL THEN 1 ELSE 0 END) AS is_favorite
        FROM verses v
        INNER JOIN chapters c ON v.chapter_id = c.id
        INNER JOIN books b ON v.book_id = b.id
        LEFT JOIN highlights h ON v.id = h.verse_id
        LEFT JOIN bookmarks bm ON v.id = bm.verse_id
        LEFT JOIN favorites fav ON v.id = fav.verse_id
        WHERE v.content_text LIKE '%' || :query || '%' AND b.testament = :testament AND v.translation = :translation
        ORDER BY b.order_index ASC, c.chapter_number ASC, v.verse_number ASC
    """)
    suspend fun searchVersesByTestament(query: String, testament: String, translation: String = "rvr1960"): List<VerseWithDetails>

    @Query("""
        SELECT v.*, b.name AS book_name, c.chapter_number AS chapter_number,
               h.color_hex AS highlight_color,
               (CASE WHEN bm.id IS NOT NULL THEN 1 ELSE 0 END) AS is_bookmarked,
               (CASE WHEN fav.id IS NOT NULL THEN 1 ELSE 0 END) AS is_favorite
        FROM verses v
        INNER JOIN chapters c ON v.chapter_id = c.id
        INNER JOIN books b ON v.book_id = b.id
        LEFT JOIN highlights h ON v.id = h.verse_id
        LEFT JOIN bookmarks bm ON v.id = bm.verse_id
        LEFT JOIN favorites fav ON v.id = fav.verse_id
        WHERE v.content_text LIKE '%' || :query || '%' AND v.book_id = :bookId AND v.translation = :translation
        ORDER BY c.chapter_number ASC, v.verse_number ASC
    """)
    suspend fun searchVersesByBook(query: String, bookId: Int, translation: String = "rvr1960"): List<VerseWithDetails>

    @Query("""
        SELECT v.*, b.name AS book_name, c.chapter_number AS chapter_number,
               h.color_hex AS highlight_color,
               (CASE WHEN bm.id IS NOT NULL THEN 1 ELSE 0 END) AS is_bookmarked,
               (CASE WHEN fav.id IS NOT NULL THEN 1 ELSE 0 END) AS is_favorite
        FROM verses v
        INNER JOIN chapters c ON v.chapter_id = c.id
        INNER JOIN books b ON v.book_id = b.id
        LEFT JOIN highlights h ON v.id = h.verse_id
        LEFT JOIN bookmarks bm ON v.id = bm.verse_id
        LEFT JOIN favorites fav ON v.id = fav.verse_id
        WHERE v.content_text LIKE '%' || :query || '%' AND v.book_id = :bookId AND c.chapter_number = :chapterNumber AND v.translation = :translation
        ORDER BY v.verse_number ASC
    """)
    suspend fun searchVersesByBookAndChapter(query: String, bookId: Int, chapterNumber: Int, translation: String = "rvr1960"): List<VerseWithDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<VerseEntity>)
}
