package com.jadalai.reinavalera1960.ui.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.jadalai.reinavalera1960.data.local.dao.VerseWithDetails
import com.jadalai.reinavalera1960.data.local.entity.BookEntity
import com.jadalai.reinavalera1960.data.local.preferences.AppPreferencesManager
import com.jadalai.reinavalera1960.data.repository.BibleRepository
import com.jadalai.reinavalera1960.ui.settings.BibleTranslation
import javax.inject.Inject

val defaultGenesisBook = BookEntity(
    id = 1,
    name = "Génesis",
    testament = "OT",
    orderIndex = 1,
    chaptersCount = 50
)

data class ReaderUiState(
    val currentBook: BookEntity = defaultGenesisBook,
    val currentChapterNumber: Int = 1,
    val currentChapterId: Int = 1001,
    val selectedVerses: List<VerseWithDetails> = emptyList(),
    val isBookPickerOpen: Boolean = false,
    val isVerseMenuOpen: Boolean = false,
    val translation: BibleTranslation = BibleTranslation.RVR1960,
    val targetVerseIdToHighlight: Long? = null
)

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val repository: BibleRepository,
    private val preferencesManager: AppPreferencesManager
) : ViewModel() {

    val books: StateFlow<List<BookEntity>> = repository.getAllBooks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _uiState = MutableStateFlow(
        ReaderUiState(
            currentChapterNumber = preferencesManager.getLastReadChapterNum(),
            currentChapterId = preferencesManager.getLastReadBookId() * 1000 + preferencesManager.getLastReadChapterNum()
        )
    )
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val currentVerses: StateFlow<List<VerseWithDetails>> = _uiState
        .flatMapLatest { state ->
            repository.getVersesWithDetailsForChapter(state.currentChapterId, state.translation.code)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val isCurrentChapterRead: StateFlow<Boolean> = _uiState
        .flatMapLatest { state ->
            repository.isChapterRead(state.currentBook.id, state.currentChapterNumber)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        viewModelScope.launch {
            repository.getAllBooks().collect { bookList ->
                if (bookList.isNotEmpty()) {
                    val savedBookId = preferencesManager.getLastReadBookId()
                    val matchingBook = bookList.find { it.id == savedBookId } ?: bookList.first()
                    val savedChapNum = preferencesManager.getLastReadChapterNum().coerceIn(1, matchingBook.chaptersCount)
                    _uiState.value = _uiState.value.copy(
                        currentBook = matchingBook,
                        currentChapterNumber = savedChapNum,
                        currentChapterId = matchingBook.id * 1000 + savedChapNum
                    )
                }
            }
        }
    }

    fun setTranslation(translation: BibleTranslation) {
        _uiState.value = _uiState.value.copy(translation = translation)
    }

    fun selectBookAndChapter(book: BookEntity, chapterNumber: Int) {
        val chapterId = book.id * 1000 + chapterNumber
        preferencesManager.saveLastReadPosition(book.id, chapterNumber)
        _uiState.value = _uiState.value.copy(
            currentBook = book,
            currentChapterNumber = chapterNumber,
            currentChapterId = chapterId,
            isBookPickerOpen = false,
            targetVerseIdToHighlight = null
        )
    }

    fun selectBookChapterAndVerse(book: BookEntity, chapterNumber: Int, verseId: Long?) {
        val chapterId = book.id * 1000 + chapterNumber
        preferencesManager.saveLastReadPosition(book.id, chapterNumber)
        _uiState.value = _uiState.value.copy(
            currentBook = book,
            currentChapterNumber = chapterNumber,
            currentChapterId = chapterId,
            isBookPickerOpen = false,
            targetVerseIdToHighlight = verseId
        )
    }

    fun clearTargetVerseHighlight() {
        _uiState.value = _uiState.value.copy(targetVerseIdToHighlight = null)
    }

    fun nextChapter() {
        val currentBook = _uiState.value.currentBook
        val currentChapNum = _uiState.value.currentChapterNumber
        if (currentChapNum < currentBook.chaptersCount) {
            selectBookAndChapter(currentBook, currentChapNum + 1)
        }
    }

    fun previousChapter() {
        val currentBook = _uiState.value.currentBook
        val currentChapNum = _uiState.value.currentChapterNumber
        if (currentChapNum > 1) {
            selectBookAndChapter(currentBook, currentChapNum - 1)
        }
    }

    fun openBookPicker() {
        _uiState.value = _uiState.value.copy(isBookPickerOpen = true)
    }

    fun closeBookPicker() {
        _uiState.value = _uiState.value.copy(isBookPickerOpen = false)
    }

    fun toggleVerseSelection(verse: VerseWithDetails) {
        val currentList = _uiState.value.selectedVerses.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.id == verse.id }
        if (existingIndex >= 0) {
            currentList.removeAt(existingIndex)
        } else {
            currentList.add(verse)
        }
        currentList.sortBy { it.verse_number }
        _uiState.value = _uiState.value.copy(
            selectedVerses = currentList,
            isVerseMenuOpen = false
        )
    }

    fun selectSingleVerse(verse: VerseWithDetails) {
        _uiState.value = _uiState.value.copy(
            selectedVerses = listOf(verse),
            isVerseMenuOpen = true
        )
    }

    fun openVerseMenu() {
        if (_uiState.value.selectedVerses.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(isVerseMenuOpen = true)
        }
    }

    fun closeVerseMenu() {
        _uiState.value = _uiState.value.copy(
            selectedVerses = emptyList(),
            isVerseMenuOpen = false
        )
    }

    fun highlightSelectedVerses(colorHex: String?) {
        val verses = _uiState.value.selectedVerses
        viewModelScope.launch {
            verses.forEach { v ->
                repository.setHighlight(v.id, colorHex)
            }
            val updated = _uiState.value.selectedVerses.map { it.copy(highlight_color = colorHex) }
            _uiState.value = _uiState.value.copy(selectedVerses = updated)
        }
    }

    fun bookmarkSelectedVerses(note: String?) {
        val verses = _uiState.value.selectedVerses
        viewModelScope.launch {
            if (verses.size > 1) {
                repository.saveMultipleVersesBookmark(verses, note)
            } else if (verses.isNotEmpty()) {
                repository.toggleBookmark(verses.first(), note)
            }
            closeVerseMenu()
        }
    }

    fun markChapterAsRead() {
        val currentBook = _uiState.value.currentBook
        val currentChapNum = _uiState.value.currentChapterNumber
        viewModelScope.launch {
            repository.recordChapterRead(currentBook.id, currentBook.name, currentChapNum)
        }
    }
}
