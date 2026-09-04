package com.jadalai.reinavalera1960.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import com.jadalai.reinavalera1960.data.local.BibleDatabase
import com.jadalai.reinavalera1960.data.local.dao.BookDao
import com.jadalai.reinavalera1960.data.local.dao.BookmarkDao
import com.jadalai.reinavalera1960.data.local.dao.ChapterDao
import com.jadalai.reinavalera1960.data.local.dao.FavoriteDao
import com.jadalai.reinavalera1960.data.local.dao.HighlightDao
import com.jadalai.reinavalera1960.data.local.dao.VerseDao
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Singleton

import android.util.JsonReader
import android.util.Log

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private val spanishBookOrder = listOf(
        "Génesis", "Éxodo", "Levítico", "Números", "Deuteronomio",
        "Josué", "Jueces", "Rut", "1 Samuel", "2 Samuel",
        "1 Reyes", "2 Reyes", "1 Crónicas", "2 Crónicas", "Esdras",
        "Nehemías", "Ester", "Job", "Salmos", "Proverbios",
        "Eclesiastés", "Cantares", "Isaías", "Jeremías", "Lamentaciones",
        "Ezequiel", "Daniel", "Oseas", "Joel", "Amos",
        "Abdías", "Jonás", "Miqueas", "Nahúm", "Habacuc",
        "Sofonías", "Hageo", "Zacarías", "Malaquías",
        "Mateo", "Marcos", "Lucas", "Juan", "Hechos",
        "Romanos", "1 Corintios", "2 Corintios", "Gálatas", "Efesios",
        "Filipenses", "Colosenses", "1 Tesalonicenses", "2 Tesalonicenses", "1 Timoteo",
        "2 Timoteo", "Tito", "Filemón", "Hebreos", "Santiago",
        "1 Pedro", "2 Pedro", "1 Juan", "2 Juan", "3 Juan",
        "Judas", "Apocalipsis"
    )

    private val englishBookNames = listOf(
        "Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",
        "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel",
        "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra",
        "Nehemiah", "Esther", "Job", "Psalms", "Proverbs",
        "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations",
        "Ezekiel", "Daniel", "Hosea", "Joel", "Amos",
        "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk",
        "Zephaniah", "Haggai", "Zechariah", "Malachi",
        "Matthew", "Mark", "Luke", "John", "Acts",
        "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians",
        "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy",
        "2 Timothy", "Titus", "Philemon", "Hebrews", "James",
        "1 Peter", "2 Peter", "1 John", "2 John", "3 John",
        "Jude", "Revelation"
    )

    private val englishBookMap: Map<String, Int> = englishBookNames.mapIndexed { index, name ->
        name.lowercase() to (index + 1)
    }.toMap() + mapOf(
        "psalm" to 19,
        "song of songs" to 22,
        "canticles" to 22,
        "1 samuel" to 9,
        "2 samuel" to 10,
        "1 kings" to 11,
        "2 kings" to 12,
        "1 chronicles" to 13,
        "2 chronicles" to 14,
        "1 corinthians" to 46,
        "2 corinthians" to 47,
        "1 thessalonians" to 52,
        "2 thessalonians" to 53,
        "1 timothy" to 54,
        "2 timothy" to 55,
        "1 peter" to 60,
        "2 peter" to 61,
        "1 john" to 62,
        "2 john" to 63,
        "3 john" to 64
    )

    @Provides
    @Singleton
    fun provideBibleDatabase(
        @ApplicationContext context: Context
    ): BibleDatabase {
        val dbBuilder = Room.databaseBuilder(
            context,
            BibleDatabase::class.java,
            "rvr1960_kjv_trigataro_v8.db"
        )

        dbBuilder.addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d("DatabaseModule", "onCreate: Populating RVR1960 and KJV databases...")
                populateFromTriGataroJson(context, db)
                populateFromKjvStreaming(context, db)
                Log.d("DatabaseModule", "onCreate: Database population complete.")
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val cursor = db.query("SELECT COUNT(*) FROM verses WHERE translation = 'kjv'")
                        var count = 0
                        if (cursor.moveToFirst()) {
                            count = cursor.getInt(0)
                        }
                        cursor.close()
                        Log.d("DatabaseModule", "onOpen: KJV verses count = $count")
                        if (count < 30000) {
                            populateFromKjvStreaming(context, db)
                        }
                    } catch (e: Exception) {
                        Log.e("DatabaseModule", "onOpen error", e)
                    }
                }
            }
        })

        return dbBuilder.fallbackToDestructiveMigration().build()
    }

    private fun populateFromTriGataroJson(context: Context, db: SupportSQLiteDatabase) {
        try {
            val inputStream = context.assets.open("Biblia_Reina_Valera_1960_Esp.json")
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            val stringBuilder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            reader.close()

            val jsonArray = JSONArray(stringBuilder.toString())
            val bookChaptersMap = mutableMapOf<Int, MutableSet<Int>>()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val bookNum = item.optInt("BoookNumber", item.optInt("BookNumber", 1))
                val chap = item.getInt("Chapter")
                bookChaptersMap.getOrPut(bookNum) { mutableSetOf() }.add(chap)
            }

            db.beginTransaction()
            try {
                for (bookId in 1..66) {
                    val bookName = if (bookId <= spanishBookOrder.size) spanishBookOrder[bookId - 1] else "Libro $bookId"
                    val testament = if (bookId <= 39) "OT" else "NT"
                    val chaptersSet = bookChaptersMap[bookId] ?: mutableSetOf(1)
                    val chaptersCount = chaptersSet.size
                    val cleanBookName = bookName.replace("'", "''")

                    db.execSQL(
                        "INSERT OR REPLACE INTO books (id, name, testament, order_index, chapters_count) " +
                                "VALUES ($bookId, '$cleanBookName', '$testament', $bookId, $chaptersCount)"
                    )

                    for (chapNum in chaptersSet) {
                        val chapterId = bookId * 1000 + chapNum
                        db.execSQL(
                            "INSERT OR REPLACE INTO chapters (id, book_id, chapter_number, verses_count) " +
                                    "VALUES ($chapterId, $bookId, $chapNum, 0)"
                        )
                    }
                }

                val stmt = db.compileStatement(
                    "INSERT OR REPLACE INTO verses (id, chapter_id, book_id, verse_number, content_text, translation) VALUES (?, ?, ?, ?, ?, ?)"
                )

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val bookId = item.optInt("BoookNumber", item.optInt("BookNumber", 1))
                    val chapterNum = item.getInt("Chapter")
                    val verseNum = item.getInt("Verse")
                    val text = item.getString("Text")

                    val chapterId = bookId * 1000 + chapterNum
                    val verseId = (bookId.toLong() * 1000000L) + (chapterNum * 1000L) + verseNum

                    stmt.bindLong(1, verseId)
                    stmt.bindLong(2, chapterId.toLong())
                    stmt.bindLong(3, bookId.toLong())
                    stmt.bindLong(4, verseNum.toLong())
                    stmt.bindString(5, text)
                    stmt.bindString(6, "rvr1960")
                    stmt.executeInsert()
                }

                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun populateFromKjvStreaming(context: Context, db: SupportSQLiteDatabase) {
        try {
            val inputStream = context.assets.open("kjv_1769.json")
            val jsonReader = JsonReader(InputStreamReader(inputStream, "UTF-8"))

            db.beginTransaction()
            try {
                val stmt = db.compileStatement(
                    "INSERT OR REPLACE INTO verses (id, chapter_id, book_id, verse_number, content_text, translation) VALUES (?, ?, ?, ?, ?, ?)"
                )

                jsonReader.beginObject()
                while (jsonReader.hasNext()) {
                    val key = jsonReader.nextName() // e.g. "Genesis 1:1"
                    val rawText = jsonReader.nextString()

                    // Clean KJV markup: "# " for paragraph, "[" and "]" for translators' italics
                    val cleanText = rawText.replace("# ", "").replace("#", "")
                        .replace("[", "").replace("]", "")

                    // Parse Book, Chapter, Verse
                    val lastSpace = key.lastIndexOf(' ')
                    if (lastSpace != -1) {
                        val bookStr = key.substring(0, lastSpace).trim()
                        val cvStr = key.substring(lastSpace + 1).trim()
                        val colonIndex = cvStr.indexOf(':')
                        if (colonIndex != -1) {
                            val chapNum = cvStr.substring(0, colonIndex).toIntOrNull() ?: 1
                            val verseNum = cvStr.substring(colonIndex + 1).toIntOrNull() ?: 1

                            val bookId = englishBookMap[bookStr.lowercase()] ?: 1
                            val chapterId = bookId * 1000 + chapNum
                            val verseId = 1000000000L + (bookId.toLong() * 1000000L) + (chapNum * 1000L) + verseNum

                            stmt.bindLong(1, verseId)
                            stmt.bindLong(2, chapterId.toLong())
                            stmt.bindLong(3, bookId.toLong())
                            stmt.bindLong(4, verseNum.toLong())
                            stmt.bindString(5, cleanText)
                            stmt.bindString(6, "kjv")
                            stmt.executeInsert()
                        }
                    }
                }
                jsonReader.endObject()
                jsonReader.close()

                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Provides
    fun provideBookDao(database: BibleDatabase): BookDao = database.bookDao()

    @Provides
    fun provideChapterDao(database: BibleDatabase): ChapterDao = database.chapterDao()

    @Provides
    fun provideVerseDao(database: BibleDatabase): VerseDao = database.verseDao()

    @Provides
    fun provideBookmarkDao(database: BibleDatabase): BookmarkDao = database.bookmarkDao()

    @Provides
    fun provideHighlightDao(database: BibleDatabase): HighlightDao = database.highlightDao()

    @Provides
    fun provideFavoriteDao(database: BibleDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    fun provideReadingHistoryDao(database: BibleDatabase): com.jadalai.reinavalera1960.data.local.dao.ReadingHistoryDao = database.readingHistoryDao()
}
