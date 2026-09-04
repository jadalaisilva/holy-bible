package com.jadalai.reinavalera1960.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.jadalai.reinavalera1960.data.local.dao.VerseWithDetails
import com.jadalai.reinavalera1960.data.local.entity.BookEntity
import com.jadalai.reinavalera1960.data.repository.BibleRepository
import com.jadalai.reinavalera1960.ui.settings.BibleTranslation
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val filterTestament: String = "ALL",
    val selectedBookId: Int? = null,
    val selectedChapterNumber: Int? = null,
    val results: List<VerseWithDetails> = emptyList(),
    val isSearching: Boolean = false,
    val hasSearched: Boolean = false,
    val translation: BibleTranslation = BibleTranslation.RVR1960
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: BibleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun setTranslation(translation: BibleTranslation) {
        _uiState.value = _uiState.value.copy(translation = translation)
        if (_uiState.value.query.isNotBlank()) {
            performSearch()
        }
    }

    fun onQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
        searchJob?.cancel()
        if (newQuery.length >= 3) {
            searchJob = viewModelScope.launch {
                delay(300)
                performSearch()
            }
        } else if (newQuery.isEmpty()) {
            _uiState.value = _uiState.value.copy(results = emptyList(), hasSearched = false)
        }
    }

    fun onFilterChange(testament: String) {
        _uiState.value = _uiState.value.copy(
            filterTestament = testament,
            selectedBookId = null,
            selectedChapterNumber = null
        )
        if (_uiState.value.query.isNotEmpty()) {
            performSearch()
        }
    }

    fun setScopedFilter(bookId: Int?, chapterNumber: Int?) {
        _uiState.value = _uiState.value.copy(
            selectedBookId = bookId,
            selectedChapterNumber = chapterNumber,
            filterTestament = "CUSTOM"
        )
        if (_uiState.value.query.isNotEmpty()) {
            performSearch()
        }
    }

    fun onBookSelected(book: BookEntity?) {
        _uiState.value = _uiState.value.copy(
            selectedBookId = book?.id,
            selectedChapterNumber = null,
            filterTestament = "ALL"
        )
        if (_uiState.value.query.isNotEmpty()) {
            performSearch()
        }
    }

    fun performSearch() {
        val query = _uiState.value.query.trim()
        if (query.isEmpty()) return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true)
            val results = repository.searchVerses(
                query = query,
                filter = _uiState.value.filterTestament,
                bookId = _uiState.value.selectedBookId,
                chapterNumber = _uiState.value.selectedChapterNumber,
                translation = _uiState.value.translation.code
            )
            _uiState.value = _uiState.value.copy(
                results = results,
                isSearching = false,
                hasSearched = true
            )
        }
    }
}
