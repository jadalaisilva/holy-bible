package com.jadalai.reinavalera1960.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.window.core.layout.WindowSizeClass
import com.jadalai.reinavalera1960.ui.bookmarks.BookmarksScreen
import com.jadalai.reinavalera1960.ui.i18n.AppStrings
import com.jadalai.reinavalera1960.ui.reader.ReaderDisplayConfig
import com.jadalai.reinavalera1960.ui.reader.ReaderScreen
import com.jadalai.reinavalera1960.ui.settings.AppLanguage
import com.jadalai.reinavalera1960.ui.settings.SettingsScreen
import com.jadalai.reinavalera1960.ui.theme.ThemeMode

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainAppShell(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    seedColor: Color,
    onSeedColorChange: (Color) -> Unit,
    dynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    blackTheme: Boolean,
    onBlackThemeChange: (Boolean) -> Unit,
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    displayConfig: ReaderDisplayConfig,
    onDisplayConfigChange: (ReaderDisplayConfig) -> Unit,
    selectedItemIndex: Int,
    onSelectNavigationIndex: (Int) -> Unit
) {
    var isReaderSearchActive by remember { mutableStateOf(false) }
    var isReaderScrollingUp by remember { mutableStateOf(true) }

    val navItems = listOf(
        NavigationItem(
            title = AppStrings.navGuide(currentLanguage),
            selectedIcon = Icons.Filled.AutoAwesome,
            unselectedIcon = Icons.Outlined.AutoAwesome
        ),
        NavigationItem(
            title = AppStrings.navRead(currentLanguage),
            selectedIcon = Icons.Filled.AutoStories,
            unselectedIcon = Icons.Outlined.AutoStories
        ),
        NavigationItem(
            title = AppStrings.navBookmarks(currentLanguage),
            selectedIcon = Icons.Filled.Bookmarks,
            unselectedIcon = Icons.Outlined.Bookmarks
        ),
        NavigationItem(
            title = AppStrings.navSettings(currentLanguage),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        )
    )

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val isExpanded = adaptiveInfo.windowSizeClass.windowWidthSizeClass == androidx.window.core.layout.WindowWidthSizeClass.EXPANDED

    val readerViewModel: com.jadalai.reinavalera1960.ui.reader.ReaderViewModel = androidx.hilt.navigation.compose.hiltViewModel()
    val books by readerViewModel.books.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Tablet / Foldable / Desktop NavigationRail Adaptive Scaffolding
            if (isExpanded) {
                NavigationRail(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Spacer(Modifier.height(16.dp))
                    navItems.forEachIndexed { index, item ->
                        NavigationRailItem(
                            selected = selectedItemIndex == index,
                            onClick = { onSelectNavigationIndex(index) },
                            icon = {
                                Icon(
                                    imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                // Main Screen Content with smooth animated transitions & predictive back handling
                BackHandler(enabled = selectedItemIndex != 1 && selectedItemIndex != 0) {
                    onSelectNavigationIndex(1)
                }

                AnimatedContent(
                    targetState = selectedItemIndex,
                    transitionSpec = {
                        if (targetState < initialState) {
                            (slideInHorizontally { -it / 3 } + fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)))
                                .togetherWith(slideOutHorizontally { it } + fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)))
                        } else {
                            (slideInHorizontally { it } + fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)))
                                .togetherWith(slideOutHorizontally { -it / 3 } + fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)))
                        }
                    },
                    label = "mainNavTransition",
                    modifier = Modifier.fillMaxSize()
                ) { targetIndex ->
                    when (targetIndex) {
                        0 -> com.jadalai.reinavalera1960.ui.daily.GuideScreen(
                            books = books,
                            currentLanguage = currentLanguage,
                            translation = displayConfig.bibleTranslation,
                            onNavigateToChapter = { book, chapNum, verseId ->
                                readerViewModel.selectBookChapterAndVerse(book, chapNum, verseId)
                                onSelectNavigationIndex(1)
                            }
                        )
                        1 -> ReaderScreen(
                            viewModel = readerViewModel,
                            currentLanguage = currentLanguage,
                            displayConfig = displayConfig,
                            onDisplayConfigChange = onDisplayConfigChange,
                            onSearchVisibilityChanged = { isReaderSearchActive = it },
                            onScrollVisibilityChanged = { isReaderScrollingUp = it }
                        )
                        2 -> BookmarksScreen(
                            currentLanguage = currentLanguage,
                            onNavigateToVerse = { bookId, chapterNum, verseId ->
                                val targetBook = books.find { it.id == bookId }
                                if (targetBook != null) {
                                    readerViewModel.selectBookChapterAndVerse(targetBook, chapterNum, verseId)
                                }
                                onSelectNavigationIndex(1)
                            }
                        )
                        3 -> SettingsScreen(
                            currentLanguage = currentLanguage,
                            onSelectLanguage = onLanguageChange,
                            themeMode = themeMode,
                            onThemeModeChange = onThemeModeChange,
                            seedColor = seedColor,
                            onSeedColorChange = onSeedColorChange,
                            dynamicColor = dynamicColor,
                            onDynamicColorChange = onDynamicColorChange,
                            blackTheme = blackTheme,
                            onBlackThemeChange = onBlackThemeChange,
                            displayConfig = displayConfig,
                            onDisplayConfigChange = onDisplayConfigChange
                        )
                    }
                }

                // Compact screen Floating Pill Bottom Navigation Toolbar overlay (Tomato / M3 Expressive style)
                if (!isExpanded) {
                    val isBottomBarVisible = !isReaderSearchActive && (selectedItemIndex != 1 || isReaderScrollingUp)
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isBottomBarVisible,
                        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) +
                                androidx.compose.animation.slideInVertically(
                                    initialOffsetY = { it },
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)
                                ),
                        exit = fadeOut(animationSpec = tween(150)) +
                                androidx.compose.animation.slideOutVertically(
                                    targetOffsetY = { it },
                                    animationSpec = tween(200)
                                ),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        Surface(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            shape = RoundedCornerShape(36.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shadowElevation = 8.dp,
                            tonalElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                navItems.forEachIndexed { index, item ->
                                    val isSelected = selectedItemIndex == index
                                    val pillBgColor by animateColorAsState(
                                        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                        label = "pillBgColor"
                                    )
                                    val contentColor by animateColorAsState(
                                        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                        label = "contentColor"
                                    )

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(26.dp))
                                            .background(pillBgColor)
                                            .clickable { onSelectNavigationIndex(index) }
                                            .padding(horizontal = if (isSelected) 18.dp else 14.dp, vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                                contentDescription = item.title,
                                                tint = contentColor,
                                                modifier = Modifier.size(22.dp)
                                            )
                                            if (isSelected) {
                                                Text(
                                                    text = item.title,
                                                    style = MaterialTheme.typography.labelLarge,
                                                    fontWeight = FontWeight.Bold,
                                                    color = contentColor
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
    }
}
