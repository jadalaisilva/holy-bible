package com.jadalai.reinavalera1960.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.jadalai.reinavalera1960.data.local.entity.BookmarkEntity
import com.jadalai.reinavalera1960.data.local.entity.FavoriteEntity
import com.jadalai.reinavalera1960.data.local.entity.HighlightEntity
import com.jadalai.reinavalera1960.data.local.entity.ReadingHistoryEntity
import com.jadalai.reinavalera1960.data.repository.BibleRepository
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val repository: BibleRepository
) : ViewModel() {

    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.getAllBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val highlights: StateFlow<List<HighlightEntity>> = repository.getAllHighlights()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<FavoriteEntity>> = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readingHistory: StateFlow<List<ReadingHistoryEntity>> = repository.getAllReadingHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            repository.deleteBookmark(bookmark)
        }
    }

    fun restoreBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            repository.saveBookmark(bookmark)
        }
    }

    fun updateBookmarkNote(bookmark: BookmarkEntity, newNote: String?) {
        viewModelScope.launch {
            val updated = bookmark.copy(customNote = newNote?.ifBlank { null })
            repository.saveBookmark(updated)
        }
    }

    fun deleteFavorite(favorite: FavoriteEntity) {
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
        }
    }

    fun deleteReadingHistory(history: ReadingHistoryEntity) {
        viewModelScope.launch {
            repository.deleteReadingHistory(history)
        }
    }

    fun clearAllReadingHistory() {
        viewModelScope.launch {
            repository.clearAllReadingHistory()
        }
    }
}
