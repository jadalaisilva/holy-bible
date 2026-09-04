package com.jadalai.reinavalera1960.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jadalai.reinavalera1960.data.local.dao.VerseWithDetails
import com.jadalai.reinavalera1960.ui.i18n.AppStrings
import com.jadalai.reinavalera1960.ui.settings.AppLanguage
import com.jadalai.reinavalera1960.ui.settings.BibleTranslation
import com.jadalai.reinavalera1960.ui.theme.GoogleSansFlexTopBarFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    currentLanguage: AppLanguage = AppLanguage.SPANISH,
    currentTranslation: BibleTranslation = BibleTranslation.RVR1960,
    onNavigateToVerse: (Int, Int, Long?) -> Unit = { _, _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()
    var isSearchActive by rememberSaveable { mutableStateOf(false) }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Memodream Screen Header with solid onBackground color
            Text(
                text = AppStrings.topBarSearchTitle(currentLanguage),
                style = TextStyle(
                    fontFamily = GoogleSansFlexTopBarFont,
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp,
                    lineHeight = 38.sp,
                    letterSpacing = (-0.5).sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            // Compose M3 SearchBar with 24.dp rounded corners
            SearchBar(
                query = uiState.query,
                onQueryChange = { viewModel.onQueryChange(it) },
                onSearch = {
                    viewModel.performSearch()
                    isSearchActive = false
                },
                active = isSearchActive,
                onActiveChange = { isSearchActive = it },
                placeholder = { Text(AppStrings.searchPlaceholder(currentLanguage)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                trailingIcon = {
                    if (uiState.query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { traversalIndex = 0f }
            ) {
                // Suggestions list inside SearchBar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = AppStrings.searchSuggestionsTitle(currentLanguage),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    currentSuggestions.forEach { suggestion ->
                        ListItem(
                            headlineContent = { Text(suggestion) },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            modifier = Modifier
                                .clickable {
                                    viewModel.onQueryChange(suggestion)
                                    viewModel.performSearch()
                                    isSearchActive = false
                                }
                                .fillMaxWidth()
                        )
                    }
                }
            }

            if (!isSearchActive) {
                // Filters
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = uiState.filterTestament == "ALL" && uiState.selectedBookId == null,
                            onClick = { viewModel.onFilterChange("ALL") },
                            label = { Text(AppStrings.filterAll(currentLanguage)) },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = uiState.filterTestament == "OT",
                            onClick = { viewModel.onFilterChange("OT") },
                            label = { Text(AppStrings.filterOT(currentLanguage)) },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = uiState.filterTestament == "NT",
                            onClick = { viewModel.onFilterChange("NT") },
                            label = { Text(AppStrings.filterNT(currentLanguage)) },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                // Results list
                SearchResultsContent(
                    uiState = uiState,
                    currentLanguage = currentLanguage,
                    onNavigateToVerse = onNavigateToVerse
                )
            }
        }
    }
}

@Composable
fun SearchResultsContent(
    uiState: SearchUiState,
    currentLanguage: AppLanguage,
    onNavigateToVerse: (Int, Int, Long?) -> Unit
) {
    when {
        uiState.isSearching -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.hasSearched && uiState.results.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = AppStrings.noResultsFound(uiState.query, currentLanguage),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        else -> {
            if (uiState.results.isNotEmpty()) {
                Text(
                    text = AppStrings.searchResultsFound(uiState.results.size, currentLanguage),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp)
            ) {
                items(uiState.results, key = { it.id }) { verse ->
                    SearchResultCard(
                        verse = verse,
                        query = uiState.query,
                        onClick = { onNavigateToVerse(verse.book_id, verse.chapter_number, verse.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(
    verse: VerseWithDetails,
    query: String,
    onClick: () -> Unit
) {
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = itemContainerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "${verse.book_name} ${verse.chapter_number}:${verse.verse_number}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))

            val annotatedText = buildAnnotatedString {
                val fullText = verse.content_text
                var currentIndex = 0
                val cleanQuery = query.trim()

                if (cleanQuery.isNotEmpty()) {
                    var matchIndex = fullText.indexOf(cleanQuery, currentIndex, ignoreCase = true)
                    while (matchIndex >= 0) {
                        append(fullText.substring(currentIndex, matchIndex))
                        withStyle(
                            style = SpanStyle(
                                background = MaterialTheme.colorScheme.primaryContainer,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            append(fullText.substring(matchIndex, matchIndex + cleanQuery.length))
                        }
                        currentIndex = matchIndex + cleanQuery.length
                        matchIndex = fullText.indexOf(cleanQuery, currentIndex, ignoreCase = true)
                    }
                }
                if (currentIndex < fullText.length) {
                    append(fullText.substring(currentIndex))
                }
            }

            Text(
                text = annotatedText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
