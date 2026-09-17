package com.jadalai.reinavalera1960.ui.reader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jadalai.reinavalera1960.data.local.dao.VerseWithDetails
import com.jadalai.reinavalera1960.service.AudioPlaybackService
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.jadalai.reinavalera1960.ui.components.BookChapterSheet
import com.jadalai.reinavalera1960.ui.components.ReaderPreferencesBottomSheet
import com.jadalai.reinavalera1960.ui.components.ReaderSearchDialog
import com.jadalai.reinavalera1960.ui.components.VerseActionsSheet
import com.jadalai.reinavalera1960.ui.daily.GuideScreen
import com.jadalai.reinavalera1960.ui.i18n.AppStrings
import com.jadalai.reinavalera1960.ui.i18n.resolveLanguage
import com.jadalai.reinavalera1960.ui.settings.AppLanguage
import com.jadalai.reinavalera1960.ui.settings.BibleTranslation
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
    androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel = hiltViewModel(),
    currentLanguage: AppLanguage = AppLanguage.SPANISH,
    displayConfig: ReaderDisplayConfig = ReaderDisplayConfig(),
    onDisplayConfigChange: (ReaderDisplayConfig) -> Unit = {},
    onSearchVisibilityChanged: (Boolean) -> Unit = {},
    onScrollVisibilityChanged: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val books by viewModel.books.collectAsState()
    val verses by viewModel.currentVerses.collectAsState()

    val isCurrentChapterRead by viewModel.isCurrentChapterRead.collectAsState()

    val listState = rememberLazyListState()
    var showPreferencesSheet by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var temporaryHighlightedVerseId by remember { mutableStateOf<Long?>(null) }
    var temporarySearchQuery by remember { mutableStateOf<String?>(null) }
    var flashStep by remember { mutableIntStateOf(0) } // 0..5 (3 full on/off flash cycles)

    LaunchedEffect(showSearchDialog) {
        onSearchVisibilityChanged(showSearchDialog)
    }

    LaunchedEffect(uiState.targetVerseIdToHighlight) {
        if (uiState.targetVerseIdToHighlight != null) {
            temporaryHighlightedVerseId = uiState.targetVerseIdToHighlight
            temporarySearchQuery = null
            viewModel.clearTargetVerseHighlight()
        }
    }

    // Screen Keep On Effect
    val activity = context as? Activity
    DisposableEffect(displayConfig.keepScreenOn) {
        if (displayConfig.keepScreenOn) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    LaunchedEffect(displayConfig.bibleTranslation) {
        viewModel.setTranslation(displayConfig.bibleTranslation)
    }

    LaunchedEffect(uiState.currentChapterId) {
        if (temporaryHighlightedVerseId == null && uiState.targetVerseIdToHighlight == null) {
            listState.scrollToItem(0)
        }
    }

    LaunchedEffect(temporaryHighlightedVerseId, verses) {
        val targetId = temporaryHighlightedVerseId
        if (targetId != null && verses.isNotEmpty()) {
            val targetIndex = verses.indexOfFirst { it.id == targetId }
            if (targetIndex >= 0) {
                // Scroll to target verse item
                listState.animateScrollToItem(targetIndex)
                // Flash 3 times: ON, OFF, ON, OFF, ON, OFF
                repeat(3) {
                    flashStep = 1 // Highlight ON
                    kotlinx.coroutines.delay(300)
                    flashStep = 0 // Highlight OFF
                    kotlinx.coroutines.delay(200)
                }
                temporaryHighlightedVerseId = null
                temporarySearchQuery = null
            }
        }
    }

    // Auto mark as read detection when reaching end of chapter
    var hasAutoMarkedThisChapter by remember(uiState.currentChapterId) { mutableStateOf(false) }
    LaunchedEffect(listState.canScrollForward, displayConfig.autoMarkAsRead, verses) {
        if (displayConfig.autoMarkAsRead && !hasAutoMarkedThisChapter && verses.isNotEmpty()) {
            if (!listState.canScrollForward && listState.firstVisibleItemIndex > 0) {
                hasAutoMarkedThisChapter = true
                viewModel.markChapterAsRead()
            }
        }
    }

    val readingAreaBgColor = displayConfig.readingBackgroundTheme.backgroundColor ?: Color.Transparent
    val defaultTextColor = displayConfig.readingBackgroundTheme.textColor ?: MaterialTheme.colorScheme.onSurface

    // Scroll direction tracking for hiding/showing top bar and bottom bar in reading
    var isScrollingUp by remember { mutableStateOf(true) }
    var previousIndex by remember { mutableIntStateOf(0) }
    var previousScrollOffset by remember { mutableIntStateOf(0) }

    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        val currentIndex = listState.firstVisibleItemIndex
        val currentOffset = listState.firstVisibleItemScrollOffset
        if (currentIndex > previousIndex) {
            isScrollingUp = false
        } else if (currentIndex < previousIndex) {
            isScrollingUp = true
        } else {
            if (currentOffset > previousScrollOffset + 12) {
                isScrollingUp = false
            } else if (currentOffset < previousScrollOffset - 12) {
                isScrollingUp = true
            }
        }
        previousIndex = currentIndex
        previousScrollOffset = currentOffset
    }

    val scrollBehavior = androidx.compose.material3.TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val isKjv = displayConfig.bibleTranslation == BibleTranslation.KJV
    val localizedCurrentBookName = remember(uiState.currentBook, isKjv) {
        AppStrings.getLocalizedBookName(uiState.currentBook.id, uiState.currentBook.name, isKjv)
    }

    LaunchedEffect(isScrollingUp) {
        onScrollVisibilityChanged(isScrollingUp)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        androidx.compose.material3.Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
            topBar = {
                androidx.compose.material3.LargeTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.openBookPicker() }
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                        ) {
                            Text(
                                text = "$localizedCurrentBookName ${uiState.currentChapterNumber}",
                                style = com.jadalai.reinavalera1960.ui.theme.LocalAppFonts.current.topBarTitle,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Seleccionar libro y capítulo",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    actions = {
                        // When verses are selected, bookmark action appears
                        AnimatedVisibility(
                            visible = uiState.selectedVerses.isNotEmpty(),
                            enter = fadeIn(animationSpec = tween(200)) + androidx.compose.animation.scaleIn(),
                            exit = fadeOut(animationSpec = tween(150)) + androidx.compose.animation.scaleOut()
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.openVerseMenu()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = "Opciones del versículo",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        IconButton(onClick = { showPreferencesSheet = true }) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Preferencias de lectura",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(onClick = { showSearchDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(readingAreaBgColor)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(if (displayConfig.separateParagraphs) 10.dp else 4.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 8.dp, bottom = 120.dp)
                ) {
                    if (verses.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.material3.LoadingIndicator(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    } else {
                        items(verses, key = { it.id }) { verse ->
                            val isSelected = uiState.selectedVerses.any { it.id == verse.id }
                            val isFlashActive = temporaryHighlightedVerseId == verse.id && flashStep == 1
                            val flashQuery = if (temporaryHighlightedVerseId == verse.id) temporarySearchQuery else null

                            VerseItem(
                                verse = verse,
                                config = displayConfig,
                                isSelected = isSelected,
                                isFlashActive = isFlashActive,
                                flashQuery = flashQuery,
                                textColor = defaultTextColor,
                                onClick = { viewModel.toggleVerseSelection(verse) },
                                onLongClick = { viewModel.selectSingleVerse(verse) }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (displayConfig.autoMarkAsRead) {
                                FilledTonalButton(
                                    onClick = {
                                        viewModel.markChapterAsRead()
                                    },
                                    enabled = !isCurrentChapterRead,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = if (isCurrentChapterRead) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = if (isCurrentChapterRead) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (isCurrentChapterRead) Icons.Default.Check else Icons.Default.DoneAll,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 6.dp).size(18.dp)
                                    )
                                    Text(
                                        text = if (isCurrentChapterRead) {
                                            if (currentLanguage == AppLanguage.ENGLISH) "Read" else "Leído"
                                        } else {
                                            if (currentLanguage == AppLanguage.ENGLISH) "Mark as read" else "Marcar como leído"
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.previousChapter() },
                                    enabled = uiState.currentChapterNumber > 1
                                ) {
                                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                                    Text(AppStrings.previousChapter(currentLanguage), modifier = Modifier.padding(start = 4.dp))
                                }

                                ElevatedButton(
                                    onClick = { viewModel.nextChapter() },
                                    enabled = uiState.currentChapterNumber < uiState.currentBook.chaptersCount
                                ) {
                                    Text(AppStrings.nextChapter(currentLanguage), modifier = Modifier.padding(end = 4.dp))
                                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showPreferencesSheet) {
            ReaderPreferencesBottomSheet(
                config = displayConfig,
                onConfigChange = onDisplayConfigChange,
                currentLanguage = currentLanguage,
                onDismiss = { showPreferencesSheet = false }
            )
        }

        if (showSearchDialog) {
            ReaderSearchDialog(
                currentLanguage = currentLanguage,
                currentTranslation = displayConfig.bibleTranslation,
                currentBookId = uiState.currentBook.id,
                currentBookName = uiState.currentBook.name,
                currentChapterNumber = uiState.currentChapterNumber,
                onSelectVerse = { bookId, chapterNum, verseId, query ->
                    val matchingBook = books.find { it.id == bookId }
                    if (matchingBook != null) {
                        viewModel.selectBookAndChapter(matchingBook, chapterNum)
                        temporaryHighlightedVerseId = verseId
                        temporarySearchQuery = query
                    }
                },
                onDismiss = { showSearchDialog = false }
            )
        }

        if (uiState.isBookPickerOpen) {
            BookChapterSheet(
                books = books,
                currentBook = uiState.currentBook,
                currentChapterNumber = uiState.currentChapterNumber,
                currentLanguage = currentLanguage,
                isEnglish = isKjv,
                onDismiss = { viewModel.closeBookPicker() },
                onSelectBookChapter = { book, chapNum ->
                    viewModel.selectBookAndChapter(book, chapNum)
                }
            )
        }

        if (uiState.isVerseMenuOpen && uiState.selectedVerses.isNotEmpty()) {
            val selectedList = uiState.selectedVerses
            VerseActionsSheet(
                verses = selectedList,
                isEnglish = isKjv,
                onDismiss = { viewModel.closeVerseMenu() },
                onHighlight = { colorHex ->
                    viewModel.highlightSelectedVerses(colorHex)
                },
                onBookmark = { note ->
                    viewModel.bookmarkSelectedVerses(note)
                },
                onShare = {
                    shareVerses(context, selectedList)
                    viewModel.closeVerseMenu()
                },
                onPlayAudio = {
                    val first = selectedList.first()
                    val versionLabel = if (displayConfig.bibleTranslation == BibleTranslation.KJV) "King James Version" else "Reina Valera 1960"
                    val intent = Intent(context, AudioPlaybackService::class.java).apply {
                        action = AudioPlaybackService.ACTION_PLAY
                        putExtra(AudioPlaybackService.EXTRA_TITLE, "${first.book_name} ${first.chapter_number}:${first.verse_number}")
                        putExtra(AudioPlaybackService.EXTRA_SUBTITLE, versionLabel)
                        putExtra(AudioPlaybackService.EXTRA_AUDIO_URL, first.audio_url ?: "https://ia800204.us.archive.org/11/items/rvr1960_audio/sample.mp3")
                    }
                    context.startService(intent)
                    viewModel.closeVerseMenu()
                },
                onPlayTts = {
                    val textToRead = selectedList.joinToString(separator = "\n") { "${it.verse_number}. ${it.content_text}" }
                    com.jadalai.reinavalera1960.service.TextToSpeechManager.getInstance(context).speak(textToRead, isKjv)
                    viewModel.closeVerseMenu()
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerseItem(
    verse: VerseWithDetails,
    config: ReaderDisplayConfig,
    isSelected: Boolean,
    isFlashActive: Boolean,
    flashQuery: String?,
    textColor: Color,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit = {},
    onLongClick: () -> Unit
) {
    val targetBgColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        isFlashActive -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
        config.showHighlights -> when (verse.highlight_color) {
            "#FFF59D" -> Color(0x66FFF59D)
            "#A5D6A7" -> Color(0x66A5D6A7)
            "#90CAF9" -> Color(0x6690CAF9)
            "#FFCCBC" -> Color(0x66FFCCBC)
            "#CE93D8" -> Color(0x66CE93D8)
            else -> Color.Transparent
        }
        else -> Color.Transparent
    }
    val animatedHighlightColor by androidx.compose.animation.animateColorAsState(
        targetValue = targetBgColor,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 200),
        label = "verseHighlightAnim"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val flashBgColor = MaterialTheme.colorScheme.primaryContainer
    val onFlashBgColor = MaterialTheme.colorScheme.onPrimaryContainer

    val styledVerseText = remember(verse.content_text, isFlashActive, flashQuery) {
        androidx.compose.ui.text.buildAnnotatedString {
            val fullText = verse.content_text
            val query = flashQuery?.trim() ?: ""
            if (!isFlashActive || query.isEmpty()) {
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
                        androidx.compose.ui.text.SpanStyle(
                            background = flashBgColor,
                            color = onFlashBgColor,
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(animatedHighlightColor)
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "${verse.verse_number}",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = if (isSelected || isFlashActive) primaryColor else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp, top = 3.dp)
            )

            Text(
                text = styledVerseText,
                textAlign = config.justification.align,
                style = TextStyle(
                    fontFamily = config.scriptureFont.fontFamily,
                    fontSize = config.fontSize,
                    lineHeight = config.lineHeight,
                    fontWeight = config.textWeight.weight,
                    color = textColor
                ),
                modifier = Modifier.weight(1f)
            )

            if (config.showBookmarks && verse.is_bookmarked) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Marcado",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

private fun shareVerses(context: Context, verses: List<VerseWithDetails>) {
    if (verses.isEmpty()) return
    val first = verses.first()
    val isMulti = verses.size > 1
    val reference = if (isMulti) {
        val last = verses.last()
        "${first.book_name} ${first.chapter_number}:${first.verse_number}-${last.verse_number}"
    } else {
        "${first.book_name} ${first.chapter_number}:${first.verse_number}"
    }

    val content = if (isMulti) {
        verses.joinToString("\n") { "${it.verse_number}. ${it.content_text}" }
    } else {
        "\"${first.content_text}\""
    }

    val shareText = "$content\n— $reference"
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "Compartir"))
}
