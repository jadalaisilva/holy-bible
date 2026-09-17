package com.jadalai.reinavalera1960.ui.bookmarks

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.MoreVert
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
import com.jadalai.reinavalera1960.ui.i18n.resolveLanguage
import com.jadalai.reinavalera1960.ui.settings.AppLanguage
import com.jadalai.reinavalera1960.ui.theme.GoogleSansFlexTopBarFont
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import java.util.Calendar
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Scaffold

enum class HistoryDateFilter {
    ALL, TODAY, THIS_WEEK, SPECIFIC_DATE;

    fun getLabel(isEn: Boolean, customDateFormatted: String? = null): String = when (this) {
        ALL -> if (isEn) "All" else "Todos"
        TODAY -> if (isEn) "Today" else "Hoy"
        THIS_WEEK -> if (isEn) "This week" else "Esta semana"
        SPECIFIC_DATE -> customDateFormatted ?: if (isEn) "Pick date..." else "Elegir fecha..."
    }
}

private fun getHistoryDateGroup(timestamp: Long, isEn: Boolean): String {
    val now = Calendar.getInstance()
    val itemCal = Calendar.getInstance().apply { timeInMillis = timestamp }

    val isSameDay = now.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == itemCal.get(Calendar.DAY_OF_YEAR)
    if (isSameDay) return if (isEn) "Today" else "Hoy"

    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val isYesterday = yesterday.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
            yesterday.get(Calendar.DAY_OF_YEAR) == itemCal.get(Calendar.DAY_OF_YEAR)
    if (isYesterday) return if (isEn) "Yesterday" else "Ayer"

    val currentWeek = now.get(Calendar.WEEK_OF_YEAR)
    val currentYear = now.get(Calendar.YEAR)
    val isThisWeek = currentYear == itemCal.get(Calendar.YEAR) &&
            currentWeek == itemCal.get(Calendar.WEEK_OF_YEAR)
    if (isThisWeek) return if (isEn) "This week" else "Esta semana"

    val locale = if (isEn) Locale.ENGLISH else Locale.forLanguageTag("es")
    val isThisYear = currentYear == itemCal.get(Calendar.YEAR)
    val pattern = if (isThisYear) "MMMM" else "MMMM yyyy"
    val formatted = SimpleDateFormat(pattern, locale).format(Date(timestamp))
    return formatted.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
}

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
    var activeHistoryFilter by rememberSaveable { mutableStateOf(HistoryDateFilter.ALL) }
    var selectedSpecificDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    val effectiveLang = resolveLanguage(currentLanguage)
    val isEn = effectiveLang == AppLanguage.ENGLISH
    val bookmarksLabel = AppStrings.topBarBookmarksTitle(currentLanguage)
    val historyLabel = if (isEn) "History" else "Historial"

    val filteredHistory: List<ReadingHistoryEntity> = remember(readingHistory, activeHistoryFilter, selectedSpecificDateMillis) {
        if (activeHistoryFilter == HistoryDateFilter.ALL) {
            readingHistory
        } else {
            val now = Calendar.getInstance()
            val specificCal = selectedSpecificDateMillis?.let {
                Calendar.getInstance().apply { timeInMillis = it }
            }
            readingHistory.filter { item ->
                val itemCal = Calendar.getInstance().apply { timeInMillis = item.timestampRead }
                when (activeHistoryFilter) {
                    HistoryDateFilter.ALL -> true
                    HistoryDateFilter.TODAY -> {
                        now.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                                now.get(Calendar.DAY_OF_YEAR) == itemCal.get(Calendar.DAY_OF_YEAR)
                    }
                    HistoryDateFilter.THIS_WEEK -> {
                        now.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                                now.get(Calendar.WEEK_OF_YEAR) == itemCal.get(Calendar.WEEK_OF_YEAR)
                    }
                    HistoryDateFilter.SPECIFIC_DATE -> {
                        if (specificCal != null) {
                            specificCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                                    specificCal.get(Calendar.DAY_OF_YEAR) == itemCal.get(Calendar.DAY_OF_YEAR)
                        } else {
                            true
                        }
                    }
                }
            }
        }
    }

    val groupedHistory: Map<String, List<ReadingHistoryEntity>> = remember(filteredHistory, isEn) {
        val map = LinkedHashMap<String, MutableList<ReadingHistoryEntity>>()
        for (item in filteredHistory) {
            val groupKey = getHistoryDateGroup(item.timestampRead, isEn)
            val list = map.getOrPut(groupKey) { mutableListOf() }
            list.add(item)
        }
        map
    }
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
            // Sub Tabs: Marcadores / Historial - M3 Expressive segmented tonal pill container
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val isBookmarks = selectedSubTab == 0
                    val bookmarksBg by animateColorAsState(
                        targetValue = if (isBookmarks) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "bookmarksTabBg"
                    )
                    val bookmarksContentColor by animateColorAsState(
                        targetValue = if (isBookmarks) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "bookmarksTabColor"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(bookmarksBg)
                            .clickable { selectedSubTab = 0 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isBookmarks) Icons.Filled.Bookmarks else Icons.Outlined.Bookmarks,
                                contentDescription = null,
                                tint = bookmarksContentColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "$bookmarksLabel (${bookmarks.size})",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isBookmarks) FontWeight.Bold else FontWeight.Medium,
                                color = bookmarksContentColor
                            )
                        }
                    }

                    val isHistory = selectedSubTab == 1
                    val historyBg by animateColorAsState(
                        targetValue = if (isHistory) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "historyTabBg"
                    )
                    val historyContentColor by animateColorAsState(
                        targetValue = if (isHistory) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "historyTabColor"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(historyBg)
                            .clickable { selectedSubTab = 1 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isHistory) Icons.Filled.History else Icons.Outlined.History,
                                contentDescription = null,
                                tint = historyContentColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "$historyLabel (${readingHistory.size})",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isHistory) FontWeight.Bold else FontWeight.Medium,
                                color = historyContentColor
                            )
                        }
                    }
                }
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
                                    currentLanguage = currentLanguage,
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
                                        shareBookmark(context, bookmark, isEn)
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // Stylized Timeline History Tab
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
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Date filter chips row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val specificDateLabel = remember(selectedSpecificDateMillis, isEn) {
                                    selectedSpecificDateMillis?.let { ms ->
                                        val locale = if (isEn) Locale.ENGLISH else Locale.forLanguageTag("es")
                                        val sdf = SimpleDateFormat(if (isEn) "MMM dd, yyyy" else "dd MMM yyyy", locale)
                                        sdf.format(Date(ms))
                                    }
                                }

                                for (filterOption in HistoryDateFilter.values()) {
                                    val isSelected = activeHistoryFilter == filterOption
                                    val chipLabel = filterOption.getLabel(isEn, specificDateLabel)
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            if (filterOption == HistoryDateFilter.SPECIFIC_DATE) {
                                                activeHistoryFilter = filterOption
                                                showDatePickerDialog = true
                                            } else {
                                                activeHistoryFilter = filterOption
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = chipLabel,
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                            )
                                        },
                                        leadingIcon = {
                                            if (filterOption == HistoryDateFilter.SPECIFIC_DATE) {
                                                Icon(
                                                    imageVector = Icons.Default.DateRange,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            } else if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        },
                                        shape = RoundedCornerShape(16.dp),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        border = null
                                    )
                                }
                            }

                            if (filteredHistory.isEmpty()) {
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
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.outline
                                        )
                                        Text(
                                            text = if (isEn) "No history for selected filter" else "Sin lecturas en el período seleccionado",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    contentPadding = PaddingValues(top = 4.dp, bottom = 120.dp)
                                ) {
                                    for ((groupTitle, itemsInGroup) in groupedHistory) {
                                        item(key = "header_$groupTitle") {
                                            HistorySectionHeader(
                                                title = groupTitle,
                                                count = itemsInGroup.size
                                            )
                                        }

                                        itemsIndexed(
                                            items = itemsInGroup,
                                            key = { _, item -> item.id }
                                        ) { index, historyItem ->
                                            val isLast = index == itemsInGroup.lastIndex
                                            HistoryTimelineItem(
                                                historyItem = historyItem,
                                                currentLanguage = currentLanguage,
                                                isLastInGroup = isLast,
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
            }
        }
    }

    // M3 Expressive DatePickerDialog for specific date filter
    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedSpecificDateMillis ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateUtc = datePickerState.selectedDateMillis
                        if (selectedDateUtc != null) {
                            // Convert UTC millis from DatePicker to local date millis for accurate day comparison
                            val utcCal = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC")).apply {
                                timeInMillis = selectedDateUtc
                            }
                            val localCal = Calendar.getInstance().apply {
                                set(
                                    utcCal.get(Calendar.YEAR),
                                    utcCal.get(Calendar.MONTH),
                                    utcCal.get(Calendar.DAY_OF_MONTH),
                                    12, 0, 0
                                )
                            }
                            selectedSpecificDateMillis = localCal.timeInMillis
                        }
                        showDatePickerDialog = false
                    }
                ) {
                    Text(if (isEn) "OK" else "Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text(if (isEn) "Cancel" else "Cancelar")
                }
            },
            shape = RoundedCornerShape(28.dp)
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }

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
        val rawBookName = bookmarkToDelete.bookName
        val bookId = (bookmarkToDelete.verseId / 1000000).toInt()
        val localizedBook = AppStrings.getLocalizedBookName(bookId, rawBookName, isEn)
        val verseRef = if (bookmarkToDelete.verseEndNumber > bookmarkToDelete.verseNumber) {
            "$localizedBook ${bookmarkToDelete.chapterNumber}:${bookmarkToDelete.verseNumber}-${bookmarkToDelete.verseEndNumber}"
        } else {
            "$localizedBook ${bookmarkToDelete.chapterNumber}:${bookmarkToDelete.verseNumber}"
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
                    text = if (isEn) "Do you want to delete the bookmark for $verseRef?" else "¿Desea eliminar el marcador de $verseRef?",
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
        val rawBookName = historyToDelete.bookName
        val bookId = historyToDelete.bookId
        val localizedBook = AppStrings.getLocalizedBookName(bookId, rawBookName, isEn)
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
                    text = if (isEn) "Do you want to remove $localizedBook ${historyToDelete.chapterNumber} from history?" else "¿Desea eliminar ${historyToDelete.bookName} ${historyToDelete.chapterNumber} del historial?",
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
        val rawBookName = currentTarget.bookName
        val bookId = (currentTarget.verseId / 1000000).toInt()
        val localizedBook = AppStrings.getLocalizedBookName(bookId, rawBookName, isEn)
        val verseRef = if (currentTarget.verseEndNumber > currentTarget.verseNumber) {
            "$localizedBook ${currentTarget.chapterNumber}:${currentTarget.verseNumber}-${currentTarget.verseEndNumber}"
        } else {
            "$localizedBook ${currentTarget.chapterNumber}:${currentTarget.verseNumber}"
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
    currentLanguage: AppLanguage = AppLanguage.SPANISH,
    onRequestDelete: () -> Unit,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onShare: () -> Unit
) {
    val effectiveLang = resolveLanguage(currentLanguage)
    val isEn = effectiveLang == AppLanguage.ENGLISH
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { totalDistance -> totalDistance * 0.5f },
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onRequestDelete()
                false
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
            val isDismissing = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart
            val containerColor = if (isDismissing) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f)
            val iconTint = if (isDismissing) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(containerColor)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = if (isEn) "Delete bookmark" else "Eliminar marcador",
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        content = {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                ElevatedCard(
                    shape = RoundedCornerShape(24.dp),
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
                            val rawBookName = bookmark.bookName
                            val bookId = (bookmark.verseId / 1000000).toInt()
                            val localizedBook = AppStrings.getLocalizedBookName(bookId, rawBookName, isEn)
                            val verseRef = if (bookmark.verseEndNumber > bookmark.verseNumber) {
                                "$localizedBook ${bookmark.chapterNumber}:${bookmark.verseNumber}-${bookmark.verseEndNumber}"
                            } else {
                                "$localizedBook ${bookmark.chapterNumber}:${bookmark.verseNumber}"
                            }
                            Text(
                                text = verseRef,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )

                            var showMenu by remember { mutableStateOf(false) }
                            Box {
                                IconButton(onClick = { showMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = if (isEn) "Options" else "Opciones",
                                        tint = MaterialTheme.colorScheme.outline
                                    )
                                }
                                androidx.compose.material3.DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }
                                ) {
                                    androidx.compose.material3.DropdownMenuItem(
                                        text = { Text(if (isEn) "Edit note" else "Editar nota") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = null
                                            )
                                        },
                                        onClick = {
                                            showMenu = false
                                            onEdit()
                                        }
                                    )
                                    androidx.compose.material3.DropdownMenuItem(
                                        text = { Text(if (isEn) "Share" else "Compartir") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Share,
                                                contentDescription = null
                                            )
                                        },
                                        onClick = {
                                            showMenu = false
                                            onShare()
                                        }
                                    )
                                    androidx.compose.material3.DropdownMenuItem(
                                        text = {
                                            Text(
                                                if (isEn) "Delete" else "Eliminar",
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        },
                                        onClick = {
                                            showMenu = false
                                            onRequestDelete()
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${bookmark.verseText}\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
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
                                    text = "${AppStrings.bookmarkNoteLabel(currentLanguage)} ${bookmark.customNote}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        val dateString = remember(bookmark.timestampAdded, isEn) {
                            val locale = if (isEn) Locale.ENGLISH else Locale.forLanguageTag("es")
                            val sdf = SimpleDateFormat(if (isEn) "MMM dd, yyyy" else "dd MMM yyyy", locale)
                            sdf.format(Date(bookmark.timestampAdded))
                        }
                        Text(
                            text = AppStrings.bookmarkSavedDate(dateString, currentLanguage),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun HistorySectionHeader(
    title: String,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 20.dp, bottom = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            modifier = Modifier.padding(bottom = 1.dp)
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
            )
        }
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 1.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryTimelineItem(
    historyItem: com.jadalai.reinavalera1960.data.local.entity.ReadingHistoryEntity,
    currentLanguage: AppLanguage = AppLanguage.SPANISH,
    isLastInGroup: Boolean = false,
    onClick: () -> Unit,
    onRequestDelete: () -> Unit
) {
    val effectiveLang = resolveLanguage(currentLanguage)
    val isEn = effectiveLang == AppLanguage.ENGLISH
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { totalDistance -> totalDistance * 0.5f },
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onRequestDelete()
                false
            } else {
                false
            }
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Timeline vertical track & node
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight()
                .padding(top = 10.dp)
        ) {
            // Node dot (Material 3 Expressive timeline bullet)
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .size(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(16.dp)
                ) {}
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(8.dp)
                ) {}
            }

            // Continuous vertical rail line
            if (!isLastInGroup) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // History entry card
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 12.dp)
        ) {
            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,
                enableDismissFromEndToStart = true,
                backgroundContent = {
                    val isDismissing = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart
                    val containerColor = if (isDismissing) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f)
                    val iconTint = if (isDismissing) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(24.dp))
                            .background(containerColor)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = if (isEn) "Remove from history" else "Eliminar del historial",
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                content = {
                    ElevatedCard(
                        shape = RoundedCornerShape(24.dp),
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
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                val localizedBook = AppStrings.getLocalizedBookName(historyItem.bookId, historyItem.bookName, isEn)
                                Text(
                                    text = "$localizedBook ${historyItem.chapterNumber}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                val dateTimeString = remember(historyItem.timestampRead, isEn) {
                                    val locale = if (isEn) Locale.ENGLISH else Locale.forLanguageTag("es")
                                    val sdf = SimpleDateFormat(if (isEn) "MMM dd, yyyy, HH:mm" else "dd MMM yyyy, HH:mm", locale)
                                    sdf.format(Date(historyItem.timestampRead))
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = dateTimeString,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }

                            var showHistoryMenu by remember { mutableStateOf(false) }
                            Box {
                                IconButton(onClick = { showHistoryMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = if (isEn) "Options" else "Opciones",
                                        tint = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                androidx.compose.material3.DropdownMenu(
                                    expanded = showHistoryMenu,
                                    onDismissRequest = { showHistoryMenu = false }
                                ) {
                                    androidx.compose.material3.DropdownMenuItem(
                                        text = {
                                            Text(
                                                if (isEn) "Remove from history" else "Eliminar del historial",
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        },
                                        onClick = {
                                            showHistoryMenu = false
                                            onRequestDelete()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

private fun shareBookmark(context: Context, bookmark: BookmarkEntity, isEn: Boolean = false) {
    val notePrefix = if (isEn) "Note" else "Nota"
    val notePart = if (!bookmark.customNote.isNullOrBlank()) "\n$notePrefix: ${bookmark.customNote}" else ""
    val rawBookName = bookmark.bookName
    val bookId = (bookmark.verseId / 1000000).toInt()
    val localizedBook = AppStrings.getLocalizedBookName(bookId, rawBookName, isEn)
    val verseRef = if (bookmark.verseEndNumber > bookmark.verseNumber) {
        "$localizedBook ${bookmark.chapterNumber}:${bookmark.verseNumber}-${bookmark.verseEndNumber}"
    } else {
        "$localizedBook ${bookmark.chapterNumber}:${bookmark.verseNumber}"
    }
    val shareText = "\"${bookmark.verseText}\"\n— $verseRef$notePart"
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }
    val chooserTitle = if (isEn) "Share bookmark" else "Compartir marcador"
    context.startActivity(Intent.createChooser(sendIntent, chooserTitle))
}
