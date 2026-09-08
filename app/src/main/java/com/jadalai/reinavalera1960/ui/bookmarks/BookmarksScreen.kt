package com.jadalai.reinavalera1960.ui.bookmarks

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.jadalai.reinavalera1960.data.local.entity.BookmarkEntity
import com.jadalai.reinavalera1960.data.local.entity.ReadingHistoryEntity
import com.jadalai.reinavalera1960.ui.i18n.AppStrings
import com.jadalai.reinavalera1960.ui.settings.AppLanguage
import com.jadalai.reinavalera1960.ui.theme.GoogleSansFlexTopBarFont
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.material3.Scaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel = hiltViewModel(),
    currentLanguage: AppLanguage = AppLanguage.SPANISH,
    onNavigateToVerse: (Int, Int, Long?) -> Unit = { _, _, _ -> }
) {
    val context = LocalContext.current
    val bookmarks by viewModel.bookmarks.collectAsState()
    val highlights by viewModel.highlights.collectAsState()
    val readingHistory by viewModel.readingHistory.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Marcadores, 1: Historial
    var editingBookmark by remember { mutableStateOf<BookmarkEntity?>(null) }
    var editedNoteText by remember { mutableStateOf("") }
    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var itemPendingDeletionBookmark by remember { mutableStateOf<BookmarkEntity?>(null) }
    var itemPendingDeletionHistory by remember { mutableStateOf<ReadingHistoryEntity?>(null) }

    val bookmarksLabel = AppStrings.topBarBookmarksTitle(currentLanguage)
    val historyLabel = if (currentLanguage == AppLanguage.ENGLISH) "History" else "Historial"
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = bookmarksLabel,
                        style = com.jadalai.reinavalera1960.ui.theme.LocalAppFonts.current.topBarTitle,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                actions = {
                    if (selectedSubTab == 1 && readingHistory.isNotEmpty()) {
                        IconButton(
                            onClick = { showClearHistoryDialog = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = if (currentLanguage == AppLanguage.ENGLISH) "Clear history" else "Borrar historial",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Sub Tabs: Marcadores / Historial
            TabRow(
                selectedTabIndex = selectedSubTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Tab(
                    selected = selectedSubTab == 0,
                    onClick = { selectedSubTab = 0 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (selectedSubTab == 0) Icons.Filled.Bookmarks else Icons.Outlined.Bookmarks,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "$bookmarksLabel (${bookmarks.size})",
                                fontWeight = if (selectedSubTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )

                Tab(
                    selected = selectedSubTab == 1,
                    onClick = { selectedSubTab = 1 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (selectedSubTab == 1) Icons.Filled.History else Icons.Outlined.History,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "$historyLabel (${readingHistory.size})",
                                fontWeight = if (selectedSubTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                if (selectedSubTab == 0) {
                    // Bookmarks Tab
                    if (bookmarks.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.HistoryEdu,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Text(
                                    text = AppStrings.emptyBookmarks(currentLanguage),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (currentLanguage == AppLanguage.ENGLISH) "Long-press a verse in the reader to bookmark it." else "Mantenga presionado un versículo en el lector para guardarlo.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    } else {
                        val highlightsMap = remember(highlights) {
                            highlights.associate { it.verseId to it.colorHex }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 120.dp)
                        ) {
                            items(bookmarks, key = { it.id }) { bookmark ->
                                val highlightColorHex = highlightsMap[bookmark.verseId.toInt()]
                                SwipeableBookmarkItem(
                                    bookmark = bookmark,
                                    highlightColorHex = highlightColorHex,
                                    onRequestDelete = {
                                        itemPendingDeletionBookmark = bookmark
                                    },
                                    onClick = {
                                        val bookId = (bookmark.verseId / 1000000).toInt()
                                        onNavigateToVerse(bookId, bookmark.chapterNumber, bookmark.verseId)
                                    },
                                    onEdit = {
                                        editingBookmark = bookmark
                                        editedNoteText = bookmark.customNote ?: ""
                                    },
                                    onShare = {
                                        shareBookmark(context, bookmark)
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // Timeline Tab
                    if (readingHistory.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.History,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Text(
                                    text = if (currentLanguage == AppLanguage.ENGLISH) "No reading history yet" else "Aún no hay historial de lectura",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (currentLanguage == AppLanguage.ENGLISH) "Chapters marked as read will appear here on a timeline." else "Los capítulos que leas o marques como leídos aparecerán aquí en una línea de tiempo.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 120.dp)
                        ) {
                            items(readingHistory, key = { it.id }) { historyItem ->
                                HistoryTimelineItem(
                                    historyItem = historyItem,
                                    onClick = {
                                        onNavigateToVerse(historyItem.bookId, historyItem.chapterNumber, null)
                                    },
                                    onRequestDelete = {
                                        itemPendingDeletionHistory = historyItem
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    val isEn = currentLanguage == AppLanguage.ENGLISH

    // Clear history confirmation dialog
    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            title = {
                Text(
                    text = if (isEn) "Clear reading history?" else "¿Borrar historial de lectura?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (isEn) "All recorded read chapters will be removed from the history." else "Se eliminarán todos los capítulos leídos registrados en el historial.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllReadingHistory()
                        showClearHistoryDialog = false
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text(if (isEn) "Clear All" else "Borrar todo")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) {
                    Text(if (isEn) "Cancel" else "Cancelar")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    // Delete bookmark confirmation dialog
    if (itemPendingDeletionBookmark != null) {
        val bookmarkToDelete = itemPendingDeletionBookmark!!
        val verseRef = if (bookmarkToDelete.verseEndNumber > bookmarkToDelete.verseNumber) {
            "${bookmarkToDelete.bookName} ${bookmarkToDelete.chapterNumber}:${bookmarkToDelete.verseNumber}-${bookmarkToDelete.verseEndNumber}"
        } else {
            "${bookmarkToDelete.bookName} ${bookmarkToDelete.chapterNumber}:${bookmarkToDelete.verseNumber}"
        }
        AlertDialog(
            onDismissRequest = { itemPendingDeletionBookmark = null },
            title = {
                Text(
                    text = if (isEn) "Delete bookmark?" else "¿Eliminar marcador?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (isEn) "Do you want to delete bookmark for $verseRef?" else "¿Deseas eliminar el marcador de $verseRef?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteBookmark(bookmarkToDelete)
                        itemPendingDeletionBookmark = null
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = AppStrings.bookmarkRemoved(currentLanguage),
                                actionLabel = AppStrings.undo(currentLanguage)
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.restoreBookmark(bookmarkToDelete)
                            }
                        }
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text(if (isEn) "Delete" else "Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemPendingDeletionBookmark = null }) {
                    Text(if (isEn) "Cancel" else "Cancelar")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    // Delete history item confirmation dialog
    if (itemPendingDeletionHistory != null) {
        val historyToDelete = itemPendingDeletionHistory!!
        AlertDialog(
            onDismissRequest = { itemPendingDeletionHistory = null },
            title = {
                Text(
                    text = if (isEn) "Remove from history?" else "¿Eliminar del historial?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (isEn) "Do you want to remove ${historyToDelete.bookName} ${historyToDelete.chapterNumber} from history?" else "¿Deseas eliminar ${historyToDelete.bookName} ${historyToDelete.chapterNumber} del historial?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteReadingHistory(historyToDelete)
                        itemPendingDeletionHistory = null
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text(if (isEn) "Delete" else "Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemPendingDeletionHistory = null }) {
                    Text(if (isEn) "Cancel" else "Cancelar")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    // Edit bookmark note dialog
    if (editingBookmark != null) {
        val currentTarget = editingBookmark!!
        val verseRef = if (currentTarget.verseEndNumber > currentTarget.verseNumber) {
            "${currentTarget.bookName} ${currentTarget.chapterNumber}:${currentTarget.verseNumber}-${currentTarget.verseEndNumber}"
        } else {
            "${currentTarget.bookName} ${currentTarget.chapterNumber}:${currentTarget.verseNumber}"
        }
        AlertDialog(
            onDismissRequest = { editingBookmark = null },
            title = {
                Text(
                    text = verseRef,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "\"${currentTarget.verseText}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = editedNoteText,
                        onValueChange = { editedNoteText = it },
                        label = { Text(if (isEn) "Custom note" else "Nota personalizada") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateBookmarkNote(currentTarget, editedNoteText)
                        editingBookmark = null
                    }
                ) {
                    Text(if (isEn) "Save" else "Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingBookmark = null }) {
                    Text(if (isEn) "Cancel" else "Cancelar")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableBookmarkItem(
    bookmark: BookmarkEntity,
    highlightColorHex: String? = null,
    onRequestDelete: () -> Unit,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onShare: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { totalDistance -> totalDistance * 0.4f },
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onRequestDelete()
                false // Do not immediately remove until confirmed in modal
            } else {
                false
            }
        }
    )

    val itemContainerColor = remember(highlightColorHex) {
        when (highlightColorHex) {
            "#FFF59D" -> Color(0x55FFF59D)
            "#A5D6A7" -> Color(0x55A5D6A7)
            "#90CAF9" -> Color(0x5590CAF9)
            "#FFCCBC" -> Color(0x55FFCCBC)
            "#CE93D8" -> Color(0x55CE93D8)
            else -> null
        }
    } ?: MaterialTheme.colorScheme.surfaceContainerHigh

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val color = MaterialTheme.colorScheme.errorContainer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar marcador",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        },
        content = {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val verseRef = if (bookmark.verseEndNumber > bookmark.verseNumber) {
                            "${bookmark.bookName} ${bookmark.chapterNumber}:${bookmark.verseNumber}-${bookmark.verseEndNumber}"
                        } else {
                            "${bookmark.bookName} ${bookmark.chapterNumber}:${bookmark.verseNumber}"
                        }
                        Text(
                            text = verseRef,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onEdit) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar nota",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            IconButton(onClick = onShare) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Compartir",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${bookmark.verseText}\"",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (!bookmark.customNote.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { onEdit() }
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Nota: ${bookmark.customNote}",
                                style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    val dateString = remember(bookmark.timestampAdded) {
                        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        sdf.format(Date(bookmark.timestampAdded))
                    }
                    Text(
                        text = "Guardado el $dateString",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryTimelineItem(
    historyItem: com.jadalai.reinavalera1960.data.local.entity.ReadingHistoryEntity,
    onClick: () -> Unit,
    onRequestDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { totalDistance -> totalDistance * 0.4f },
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onRequestDelete()
                false // Do not immediately remove until confirmed in modal
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val color = MaterialTheme.colorScheme.errorContainer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar del historial",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        },
        content = {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Timeline badge
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${historyItem.bookName} ${historyItem.chapterNumber}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        val dateTimeString = remember(historyItem.timestampRead) {
                            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                            sdf.format(Date(historyItem.timestampRead))
                        }
                        Text(
                            text = "Leído: $dateTimeString",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    )
}

private fun shareBookmark(context: Context, bookmark: BookmarkEntity) {
    val notePart = if (!bookmark.customNote.isNullOrBlank()) "\nNota: ${bookmark.customNote}" else ""
    val shareText = "\"${bookmark.verseText}\"\n— ${bookmark.bookName} ${bookmark.chapterNumber}:${bookmark.verseNumber}$notePart"
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "Compartir marcador"))
}
