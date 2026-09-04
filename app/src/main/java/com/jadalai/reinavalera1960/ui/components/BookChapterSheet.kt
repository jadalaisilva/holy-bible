package com.jadalai.reinavalera1960.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jadalai.reinavalera1960.data.local.entity.BookEntity
import java.util.Calendar

data class ThemeGuideCategory(
    val titleEs: String,
    val titleEn: String,
    val icon: ImageVector,
    val bookId: Int,
    val bookName: String,
    val chapterNumber: Int,
    val verseNumber: Int,
    val sampleTextEs: String,
    val sampleTextEn: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookChapterSheet(
    books: List<BookEntity>,
    currentBook: BookEntity?,
    currentChapterNumber: Int,
    currentLanguage: com.jadalai.reinavalera1960.ui.settings.AppLanguage = com.jadalai.reinavalera1960.ui.settings.AppLanguage.SPANISH,
    isEnglish: Boolean = false,
    onDismiss: () -> Unit,
    onSelectBookChapter: (BookEntity, Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedBook by remember { mutableStateOf(currentBook ?: books.firstOrNull()) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Libros, 1: Capítulos
    var testamentFilter by remember { mutableStateOf<String?>("ALL") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(16.dp)
        ) {
            val effectiveLang = com.jadalai.reinavalera1960.ui.i18n.resolveLanguage(currentLanguage)
            val isEnglishUI = effectiveLang == com.jadalai.reinavalera1960.ui.settings.AppLanguage.ENGLISH

            // Tab row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text(if (isEnglishUI) "Books" else "Libros", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text(if (isEnglishUI) "Chapters" else "Capítulos", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (selectedTab) {
                0 -> {
                    // Books tab content
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        FilterChip(
                            selected = testamentFilter == "ALL",
                            onClick = { testamentFilter = "ALL" },
                            label = { Text(if (isEnglishUI) "All" else "Todos") }
                        )
                        FilterChip(
                            selected = testamentFilter == "OT",
                            onClick = { testamentFilter = "OT" },
                            label = { Text(if (isEnglishUI) "Old Testament" else "Antiguo") }
                        )
                        FilterChip(
                            selected = testamentFilter == "NT",
                            onClick = { testamentFilter = "NT" },
                            label = { Text(if (isEnglishUI) "New Testament" else "Nuevo") }
                        )
                    }

                    val filteredBooks = remember(books, testamentFilter) {
                        when (testamentFilter) {
                            "OT" -> books.filter { it.testament == "OT" }
                            "NT" -> books.filter { it.testament == "NT" }
                            else -> books
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(filteredBooks, key = { it.id }) { book ->
                            val isSelected = book.id == selectedBook?.id
                            val displayName = com.jadalai.reinavalera1960.ui.i18n.AppStrings.getLocalizedBookName(book.id, book.name, isEnglish)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                        else MaterialTheme.colorScheme.surfaceContainerLow
                                    )
                                    .clickable {
                                        selectedBook = book
                                        selectedTab = 1
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = displayName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Text(
                                        text = if (isEnglishUI) "${book.chaptersCount} ch." else "${book.chaptersCount} cap.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Chapters tab content
                    val chaptersCount = selectedBook?.chaptersCount ?: 1
                    val selectedBookDisplayName = selectedBook?.let {
                        com.jadalai.reinavalera1960.ui.i18n.AppStrings.getLocalizedBookName(it.id, it.name, isEnglish)
                    } ?: ""
                    Text(
                        text = if (isEnglishUI) "Select a chapter from $selectedBookDisplayName" else "Selecciona un capítulo de $selectedBookDisplayName",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 60.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(chaptersCount) { index ->
                            val chapterNum = index + 1
                            val isCurrent = selectedBook?.id == currentBook?.id && chapterNum == currentChapterNumber

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (isCurrent) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceContainerHigh
                                    )
                                    .clickable {
                                        selectedBook?.let { onSelectBookChapter(it, chapterNum) }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$chapterNum",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isCurrent) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
