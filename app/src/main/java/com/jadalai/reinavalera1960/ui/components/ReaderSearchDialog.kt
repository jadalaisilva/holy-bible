package com.jadalai.reinavalera1960.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jadalai.reinavalera1960.ui.i18n.AppStrings
import com.jadalai.reinavalera1960.ui.i18n.resolveLanguage
import com.jadalai.reinavalera1960.ui.search.SearchViewModel
import com.jadalai.reinavalera1960.ui.settings.AppLanguage
import com.jadalai.reinavalera1960.ui.settings.BibleTranslation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSearchDialog(
    currentLanguage: AppLanguage,
    currentTranslation: BibleTranslation,
    currentBookId: Int,
    currentBookName: String,
    currentChapterNumber: Int,
    onSelectVerse: (bookId: Int, chapterNum: Int, verseId: Long, query: String) -> Unit,
    onDismiss: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isSearchActive by rememberSaveable { mutableStateOf(true) }
    val isEn = resolveLanguage(currentLanguage) == AppLanguage.ENGLISH

    LaunchedEffect(currentTranslation) {
        viewModel.setTranslation(currentTranslation)
    }

    val defaultSuggestions = if (currentTranslation == BibleTranslation.KJV) {
        listOf("God", "Love", "Faith", "Peace", "Jesus", "Light", "Grace", "Salvation", "Hope", "Truth")
    } else {
        listOf("Dios", "Amor", "Fe", "Paz", "Jesús", "Luz", "Gracia", "Salvación", "Esperanza", "Verdad")
    }

    val currentSuggestions = if (uiState.query.isNotBlank()) {
        defaultSuggestions.filter { it.contains(uiState.query, ignoreCase = true) }
    } else {
        defaultSuggestions
    }

    BackHandler {
        onDismiss()
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        try {
            focusRequester.requestFocus()
        } catch (_: Exception) {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            query = uiState.query,
            onQueryChange = { viewModel.onQueryChange(it) },
            onSearch = { viewModel.performSearch() },
            active = isSearchActive,
            onActiveChange = { active ->
                isSearchActive = active
                if (!active) onDismiss()
            },
            placeholder = { Text(AppStrings.searchPlaceholder(currentLanguage)) },
            leadingIcon = {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
            },
            trailingIcon = {
                if (uiState.query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                    }
                }
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Testament & Current Context Filter Chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = uiState.filterTestament == "ALL" && uiState.selectedBookId == null,
                            onClick = { viewModel.onFilterChange("ALL") },
                            label = { Text(AppStrings.filterAll(currentLanguage)) }
                        )
                    }
                    item {
                        FilterChip(
                            selected = uiState.selectedBookId == currentBookId && uiState.selectedChapterNumber == currentChapterNumber,
                            onClick = {
                                if (uiState.selectedBookId == currentBookId && uiState.selectedChapterNumber == currentChapterNumber) {
                                    viewModel.onFilterChange("ALL")
                                } else {
                                    viewModel.setScopedFilter(currentBookId, currentChapterNumber)
                                }
                            },
                            label = { Text("$currentBookName $currentChapterNumber") }
                        )
                    }
                    item {
                        FilterChip(
                            selected = uiState.selectedBookId == currentBookId && uiState.selectedChapterNumber == null,
                            onClick = {
                                if (uiState.selectedBookId == currentBookId && uiState.selectedChapterNumber == null) {
                                    viewModel.onFilterChange("ALL")
                                } else {
                                    viewModel.setScopedFilter(currentBookId, null)
                                }
                            },
                            label = { Text(currentBookName) }
                        )
                    }
                    item {
                        FilterChip(
                            selected = uiState.filterTestament == "OT",
                            onClick = { viewModel.onFilterChange("OT") },
                            label = { Text(AppStrings.filterOT(currentLanguage)) }
                        )
                    }
                    item {
                        FilterChip(
                            selected = uiState.filterTestament == "NT",
                            onClick = { viewModel.onFilterChange("NT") },
                            label = { Text(AppStrings.filterNT(currentLanguage)) }
                        )
                    }
                }

                if (uiState.query.isEmpty() && !uiState.hasSearched) {
                    Text(
                        text = AppStrings.searchSuggestionsTitle(currentLanguage),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(currentSuggestions) { suggestion ->
                            ElevatedCard(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onQueryChange(suggestion)
                                        viewModel.performSearch()
                                    }
                            ) {
                                ListItem(
                                    headlineContent = { Text(suggestion, fontWeight = FontWeight.SemiBold) },
                                    leadingContent = {
                                        Icon(
                                            Icons.AutoMirrored.Filled.TrendingUp,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                                )
                            }
                        }
                    }
                } else {
                    if (uiState.hasSearched) {
                        Text(
                            text = AppStrings.searchResultsFound(uiState.results.size, currentLanguage),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    val highlightColor = MaterialTheme.colorScheme.primaryContainer
                    val primaryColor = MaterialTheme.colorScheme.primary

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.results, key = { it.id }) { verse ->
                            val annotatedText = remember(verse.content_text, uiState.query) {
                                buildAnnotatedString {
                                    val fullText = verse.content_text
                                    val query = uiState.query.trim()
                                    if (query.isEmpty()) {
                                        append(fullText)
                                    } else {
                                        var startIndex = 0
                                        while (startIndex < fullText.length) {
                                            val index = fullText.indexOf(query, startIndex, ignoreCase = true)
                                            if (index == -1) {
                                                append(fullText.substring(startIndex))
                                                break
                                            }
                                            append(fullText.substring(startIndex, index))
                                            val match = fullText.substring(index, index + query.length)
                                            withStyle(
                                                SpanStyle(
                                                    background = highlightColor,
                                                    color = primaryColor,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            ) {
                                                append(match)
                                            }
                                            startIndex = index + query.length
                                        }
                                    }
                                }
                            }

                            val itemContainerColor = remember(verse.highlight_color) {
                                when (verse.highlight_color) {
                                    "#FFF59D" -> Color(0x55FFF59D)
                                    "#A5D6A7" -> Color(0x55A5D6A7)
                                    "#90CAF9" -> Color(0x5590CAF9)
                                    "#FFCCBC" -> Color(0x55FFCCBC)
                                    "#CE93D8" -> Color(0x55CE93D8)
                                    else -> null
                                }
                            } ?: MaterialTheme.colorScheme.surfaceContainerHigh

                            ElevatedCard(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = itemContainerColor
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelectVerse(verse.book_id, verse.chapter_number, verse.id, uiState.query.trim())
                                        onDismiss()
                                    }
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    val isEnglishTrans = currentTranslation == BibleTranslation.KJV
                                    val localizedBook = AppStrings.getLocalizedBookName(verse.book_id, verse.book_name, isEnglishTrans)
                                    Text(
                                        text = "$localizedBook ${verse.chapter_number}:${verse.verse_number}",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = annotatedText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
