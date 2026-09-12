package com.jadalai.reinavalera1960.data.repository

import kotlinx.coroutines.flow.Flow
import com.jadalai.reinavalera1960.data.local.dao.BookDao
import com.jadalai.reinavalera1960.data.local.dao.BookmarkDao
import com.jadalai.reinavalera1960.data.local.dao.ChapterDao
import com.jadalai.reinavalera1960.data.local.dao.FavoriteDao
import com.jadalai.reinavalera1960.data.local.dao.HighlightDao
import com.jadalai.reinavalera1960.data.local.dao.ReadingHistoryDao
import com.jadalai.reinavalera1960.data.local.dao.VerseDao
import com.jadalai.reinavalera1960.data.local.dao.VerseWithDetails
import com.jadalai.reinavalera1960.data.local.entity.BookEntity
import com.jadalai.reinavalera1960.data.local.entity.BookmarkEntity
import com.jadalai.reinavalera1960.data.local.entity.ChapterEntity
import com.jadalai.reinavalera1960.data.local.entity.FavoriteEntity
import com.jadalai.reinavalera1960.data.local.entity.HighlightEntity
import com.jadalai.reinavalera1960.data.local.entity.ReadingHistoryEntity
import com.jadalai.reinavalera1960.data.local.entity.VerseEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BibleRepository @Inject constructor(
    private val bookDao: BookDao,
    private val chapterDao: ChapterDao,
    private val verseDao: VerseDao,
    private val bookmarkDao: BookmarkDao,
    private val highlightDao: HighlightDao,
    private val favoriteDao: FavoriteDao,
    private val readingHistoryDao: ReadingHistoryDao
) {
    // Books
    fun getAllBooks(): Flow<List<BookEntity>> = bookDao.getAllBooks()
    fun getBooksByTestament(testament: String): Flow<List<BookEntity>> = bookDao.getBooksByTestament(testament)
    suspend fun getBookById(bookId: Int): BookEntity? = bookDao.getBookById(bookId)

    // Chapters
    fun getChaptersForBook(bookId: Int): Flow<List<ChapterEntity>> = chapterDao.getChaptersForBook(bookId)
    suspend fun getChapterById(chapterId: Int): ChapterEntity? = chapterDao.getChapterById(chapterId)
    suspend fun getChapterByBookAndNumber(bookId: Int, chapterNumber: Int): ChapterEntity? =
        chapterDao.getChapterByBookAndNumber(bookId, chapterNumber)

    // Verses
    fun getVersesWithDetailsForChapter(chapterId: Int, translation: String = "rvr1960"): Flow<List<VerseWithDetails>> =
        verseDao.getVersesWithDetailsForChapter(chapterId, translation)

    fun getVersesWithDetailsForRange(
        bookId: Int,
        chapterNumber: Int,
        startVerse: Int,
        endVerse: Int,
        translation: String = "rvr1960"
    ): Flow<List<VerseWithDetails>> =
        verseDao.getVersesWithDetailsForRange(bookId, chapterNumber, startVerse, endVerse, translation)

    suspend fun getVerseById(verseId: Long): VerseEntity? = verseDao.getVerseById(verseId)

    suspend fun searchVerses(
        query: String,
        filter: String?,
        bookId: Int?,
        chapterNumber: Int? = null,
        translation: String = "rvr1960"
    ): List<VerseWithDetails> {
        return when {
            bookId != null && chapterNumber != null -> verseDao.searchVersesByBookAndChapter(query, bookId, chapterNumber, translation)
            bookId != null -> verseDao.searchVersesByBook(query, bookId, translation)
            filter == "OT" || filter == "NT" -> verseDao.searchVersesByTestament(query, filter, translation)
            else -> verseDao.searchVerses(query, translation)
        }
    }

    // Bookmarks
    fun getAllBookmarks(): Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()

    suspend fun saveBookmark(bookmark: BookmarkEntity) = bookmarkDao.insertBookmark(bookmark)

    suspend fun deleteBookmark(bookmark: BookmarkEntity) = bookmarkDao.deleteBookmark(bookmark)

    suspend fun toggleBookmark(verse: VerseWithDetails, customNote: String? = null) {
        val existing = bookmarkDao.getBookmarkByVerseId(verse.id)
        if (existing != null) {
            bookmarkDao.deleteBookmarkByVerseId(verse.id)
        } else {
            val entity = BookmarkEntity(
                verseId = verse.id,
                bookName = verse.book_name,
                chapterNumber = verse.chapter_number,
                verseNumber = verse.verse_number,
                verseEndNumber = verse.verse_number,
                verseText = verse.content_text,
                customNote = customNote
            )
            bookmarkDao.insertBookmark(entity)
        }
    }

    suspend fun saveMultipleVersesBookmark(verses: List<VerseWithDetails>, customNote: String? = null) {
        if (verses.isEmpty()) return
        val sorted = verses.sortedBy { it.verse_number }
        val first = sorted.first()
        val last = sorted.last()
        val combinedText = sorted.joinToString(" ") { "${it.verse_number}. ${it.content_text}" }
        val entity = BookmarkEntity(
            verseId = first.id,
            bookName = first.book_name,
            chapterNumber = first.chapter_number,
            verseNumber = first.verse_number,
            verseEndNumber = last.verse_number,
            verseText = combinedText,
            customNote = customNote
        )
        bookmarkDao.insertBookmark(entity)
    }

    // Highlights
    fun getAllHighlights(): Flow<List<HighlightEntity>> = highlightDao.getAllHighlights()

    suspend fun setHighlight(verseId: Long, colorHex: String?) {
        val intId = verseId.toInt()
        if (colorHex == null) {
            highlightDao.deleteHighlightByVerseId(intId)
        } else {
            val entity = HighlightEntity(
                verseId = intId,
                colorHex = colorHex
            )
            highlightDao.insertHighlight(entity)
        }
    }

    // Favorites
    fun getAllFavorites(): Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()

    suspend fun deleteFavorite(favorite: FavoriteEntity) = favoriteDao.deleteFavorite(favorite)

    suspend fun toggleFavorite(verse: VerseWithDetails) {
        val intId = verse.id.toInt()
        val existing = favoriteDao.getFavoriteByVerseId(intId)
        if (existing != null) {
            favoriteDao.deleteFavoriteByVerseId(intId)
        } else {
            val entity = FavoriteEntity(
                verseId = intId,
                bookName = verse.book_name,
                chapterNumber = verse.chapter_number,
                verseNumber = verse.verse_number,
                verseText = verse.content_text
            )
            favoriteDao.insertFavorite(entity)
        }
    }

    // Reading History
    fun getAllReadingHistory(): Flow<List<ReadingHistoryEntity>> = readingHistoryDao.getAllHistory()

    fun isChapterRead(bookId: Int, chapterNumber: Int): Flow<Boolean> =
        readingHistoryDao.hasReadChapter(bookId, chapterNumber)

    suspend fun recordChapterRead(bookId: Int, bookName: String, chapterNumber: Int) {
        val existing = readingHistoryDao.getReadChapter(bookId, chapterNumber)
        if (existing == null) {
            val entry = ReadingHistoryEntity(
                bookId = bookId,
                bookName = bookName,
                chapterNumber = chapterNumber,
                timestampRead = System.currentTimeMillis()
            )
            readingHistoryDao.insertHistory(entry)
        }
    }

    suspend fun deleteReadingHistory(history: ReadingHistoryEntity) {
        readingHistoryDao.deleteHistory(history)
    }

    suspend fun clearAllReadingHistory() {
        readingHistoryDao.clearAllHistory()
    }
}
