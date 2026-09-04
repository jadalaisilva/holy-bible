package com.jadalai.reinavalera1960.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jadalai.reinavalera1960.data.local.dao.BookDao
import com.jadalai.reinavalera1960.data.local.dao.BookmarkDao
import com.jadalai.reinavalera1960.data.local.dao.ChapterDao
import com.jadalai.reinavalera1960.data.local.dao.FavoriteDao
import com.jadalai.reinavalera1960.data.local.dao.HighlightDao
import com.jadalai.reinavalera1960.data.local.dao.ReadingHistoryDao
import com.jadalai.reinavalera1960.data.local.dao.VerseDao
import com.jadalai.reinavalera1960.data.local.entity.BookEntity
import com.jadalai.reinavalera1960.data.local.entity.BookmarkEntity
import com.jadalai.reinavalera1960.data.local.entity.ChapterEntity
import com.jadalai.reinavalera1960.data.local.entity.FavoriteEntity
import com.jadalai.reinavalera1960.data.local.entity.HighlightEntity
import com.jadalai.reinavalera1960.data.local.entity.ReadingHistoryEntity
import com.jadalai.reinavalera1960.data.local.entity.VerseEntity

@Database(
    entities = [
        BookEntity::class,
        ChapterEntity::class,
        VerseEntity::class,
        BookmarkEntity::class,
        HighlightEntity::class,
        FavoriteEntity::class,
        ReadingHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BibleDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun verseDao(): VerseDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun highlightDao(): HighlightDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun readingHistoryDao(): ReadingHistoryDao
}
