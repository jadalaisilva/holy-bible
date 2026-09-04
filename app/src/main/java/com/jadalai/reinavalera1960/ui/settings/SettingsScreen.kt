package com.jadalai.reinavalera1960.ui.settings

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jadalai.reinavalera1960.ui.components.ConnectedButtonGroup
import com.jadalai.reinavalera1960.ui.components.ThemePickerExpressiveGroup
import com.jadalai.reinavalera1960.ui.i18n.AppStrings
import com.jadalai.reinavalera1960.ui.i18n.resolveLanguage
import com.jadalai.reinavalera1960.ui.reader.ReaderDisplayConfig
import com.jadalai.reinavalera1960.ui.reader.TextJustification
import com.jadalai.reinavalera1960.ui.reader.TextWeightOption
import com.jadalai.reinavalera1960.ui.theme.ColorPreset
import com.jadalai.reinavalera1960.ui.theme.GoogleSansFlexTopBarFont
import com.jadalai.reinavalera1960.ui.theme.ReadingBackgroundTheme
import com.jadalai.reinavalera1960.ui.theme.ScriptureFont
import com.jadalai.reinavalera1960.ui.theme.ThemeMode

enum class AppLanguage(val labelEs: String, val labelEn: String, val code: String) {
    SYSTEM("Predeterminado del sistema", "System default", "system"),
    SPANISH("Español", "Spanish", "es"),
    ENGLISH("English", "English", "en")
}

enum class BibleTranslation(val labelEs: String, val labelEn: String, val code: String, val descEs: String, val descEn: String) {
    RVR1960("Reina-Valera 1960", "Reina-Valera 1960", "rvr1960", "Texto en español", "Spanish text"),
    KJV("King James Version (KJV 1769)", "King James Version (KJV 1769)", "kjv", "Texto en inglés", "English text")
}

enum class SettingsSubscreen {
    MAIN,
    READER_FORMATTING,
    ABOUT
}

@Composable
fun SettingsScreen(
    currentLanguage: AppLanguage = AppLanguage.SPANISH,
    onSelectLanguage: (AppLanguage) -> Unit = {},
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    onThemeModeChange: (ThemeMode) -> Unit = {},
    seedColor: Color = Color.White,
    onSeedColorChange: (Color) -> Unit = {},
    dynamicColor: Boolean = true,
    onDynamicColorChange: (Boolean) -> Unit = {},
    blackTheme: Boolean = false,
    onBlackThemeChange: (Boolean) -> Unit = {},
    displayConfig: ReaderDisplayConfig = ReaderDisplayConfig(),
    onDisplayConfigChange: (ReaderDisplayConfig) -> Unit = {}
) {
    var activeSubscreen by rememberSaveable { mutableStateOf(SettingsSubscreen.MAIN) }

    // Intercept back gesture on sub-screens so it navigates back inside Settings instead of closing app
    BackHandler(enabled = activeSubscreen != SettingsSubscreen.MAIN) {
        activeSubscreen = SettingsSubscreen.MAIN
    }

    AnimatedContent(
        targetState = activeSubscreen,
        transitionSpec = {
            if (targetState == SettingsSubscreen.MAIN) {
                // Navigating back
                (slideInHorizontally { -it / 3 } + fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)))
                    .togetherWith(slideOutHorizontally { it } + fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)))
            } else {
                // Navigating forward into subscreen
                (slideInHorizontally { it } + fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)))
                    .togetherWith(slideOutHorizontally { -it / 3 } + fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)))
            }
        },
        label = "subscreenTransition"
    ) { subscreen ->
        when (subscreen) {
            SettingsSubscreen.MAIN -> {
                SettingsMainList(
                    currentLanguage = currentLanguage,
                    onSelectLanguage = onSelectLanguage,
                    displayConfig = displayConfig,
                    onDisplayConfigChange = onDisplayConfigChange,
                    themeMode = themeMode,
                    onThemeModeChange = onThemeModeChange,
                    seedColor = seedColor,
                    onSeedColorChange = onSeedColorChange,
                    dynamicColor = dynamicColor,
                    onDynamicColorChange = onDynamicColorChange,
                    blackTheme = blackTheme,
                    onBlackThemeChange = onBlackThemeChange,
                    onNavigateToReaderFormatting = { activeSubscreen = SettingsSubscreen.READER_FORMATTING },
                    onNavigateToAbout = { activeSubscreen = SettingsSubscreen.ABOUT }
                )
            }
            SettingsSubscreen.READER_FORMATTING -> {
                ReaderFormattingSubscreen(
                    currentLanguage = currentLanguage,
                    displayConfig = displayConfig,
                    onDisplayConfigChange = onDisplayConfigChange,
                    onBack = { activeSubscreen = SettingsSubscreen.MAIN }
                )
            }
            SettingsSubscreen.ABOUT -> {
                AboutSubscreen(
                    currentLanguage = currentLanguage,
                    onBack = { activeSubscreen = SettingsSubscreen.MAIN }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsMainList(
    currentLanguage: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit,
    displayConfig: ReaderDisplayConfig,
    onDisplayConfigChange: (ReaderDisplayConfig) -> Unit,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    seedColor: Color,
    onSeedColorChange: (Color) -> Unit,
    dynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    blackTheme: Boolean,
    onBlackThemeChange: (Boolean) -> Unit,
    onNavigateToReaderFormatting: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    val effectiveLang = resolveLanguage(currentLanguage)
    val isEn = effectiveLang == AppLanguage.ENGLISH
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var showLanguageSheet by remember { mutableStateOf(false) }
    var showTranslationSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = AppStrings.topBarSettingsTitle(currentLanguage),
                        style = com.jadalai.reinavalera1960.ui.theme.LocalAppFonts.current.topBarTitle,
                        color = MaterialTheme.colorScheme.onBackground
                    )
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Section: Appearance
            item {
                Text(
                    text = if (isEn) "Appearance" else "Apariencia",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
            }

            item {
                com.jadalai.reinavalera1960.ui.components.ThemePickerListItem(
                    themeMode = themeMode,
                    items = 3,
                    index = 0,
                    isEn = isEn,
                    onThemeChange = onThemeModeChange
                )
            }

            item {
                com.jadalai.reinavalera1960.ui.components.ColorSchemePickerListItem(
                    currentColor = seedColor,
                    dynamicColor = dynamicColor,
                    items = 3,
                    index = 1,
                    isEn = isEn,
                    onDynamicColorChange = onDynamicColorChange,
                    onColorChange = onSeedColorChange
                )
            }

            item {
                androidx.compose.material3.ListItem(
                    modifier = Modifier
                        .clip(com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape(2, 3))
                        .clickable { onBlackThemeChange(!blackTheme) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.FormatPaint,
                            contentDescription = null
                        )
                    },
                    headlineContent = { Text(if (isEn) "Pure Black AMOLED" else "Negro Puro AMOLED") },
                    supportingContent = {
                        Text(if (isEn) "Deep black background on dark theme for OLED displays" else "Fondo negro profundo en tema oscuro para pantallas OLED")
                    },
                    trailingContent = {
                        Switch(
                            checked = blackTheme,
                            onCheckedChange = { onBlackThemeChange(it) },
                            thumbContent = {
                                if (blackTheme) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(androidx.compose.material3.SwitchDefaults.IconSize),
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        modifier = Modifier.size(androidx.compose.material3.SwitchDefaults.IconSize),
                                    )
                                }
                            },
                            colors = com.jadalai.reinavalera1960.ui.theme.CustomColors.switchColors
                        )
                    },
                    colors = com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
                )
            }

            // Section 2: Reader Formatting subscreen button
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (isEn) "Reader" else "Lectura",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }

            item {
                androidx.compose.material3.ListItem(
                    modifier = Modifier
                        .clip(com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape(0, 1))
                        .clickable { onNavigateToReaderFormatting() },
                    headlineContent = {
                        Text(
                            if (isEn) "Reader formatting & typography" else "Formato de lectura y tipografía",
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    supportingContent = {
                        Text(
                            "${if (isEn) displayConfig.scriptureFont.displayNameEn else displayConfig.scriptureFont.displayNameEs} • ${displayConfig.fontSizeSp.toInt()} sp • ${if (isEn) displayConfig.readingBackgroundTheme.displayNameEn else displayConfig.readingBackgroundTheme.displayNameEs}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.FontDownload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
                )
            }

            // Section: Language and translation
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = AppStrings.languageSection(currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }

            item {
                androidx.compose.material3.ListItem(
                    modifier = Modifier
                        .clip(com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape(0, 2))
                        .clickable { showLanguageSheet = true },
                    headlineContent = { Text(AppStrings.appLanguage(currentLanguage), fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text(if (isEn) currentLanguage.labelEn else currentLanguage.labelEs) },
                    leadingContent = {
                        Icon(
                            Icons.Default.Language,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
                )
            }

            item {
                androidx.compose.material3.ListItem(
                    modifier = Modifier
                        .clip(com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape(1, 2))
                        .clickable { showTranslationSheet = true },
                    headlineContent = { Text(AppStrings.bibleVersion(currentLanguage), fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text(if (isEn) displayConfig.bibleTranslation.labelEn else displayConfig.bibleTranslation.labelEs) },
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
                )
            }

            // Section: About
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = AppStrings.aboutSection(currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }

            item {
                androidx.compose.material3.ListItem(
                    modifier = Modifier
                        .clip(com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape(0, 1))
                        .clickable { onNavigateToAbout() },
                    headlineContent = {
                        Text(
                            AppStrings.aboutSection(currentLanguage),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingContent = {
                        Text(
                            "Santa Biblia • Version 1.0.0",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
                )
            }

            item {
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }

    if (showLanguageSheet) {
        SelectionSheet(
            title = AppStrings.appLanguage(currentLanguage),
            items = AppLanguage.values().toList(),
            selected = currentLanguage,
            labelProvider = { if (isEn) it.labelEn else it.labelEs },
            onSelect = {
                onSelectLanguage(it)
                showLanguageSheet = false
            },
            onDismiss = { showLanguageSheet = false }
        )
    }

    if (showTranslationSheet) {
        SelectionSheet(
            title = AppStrings.bibleVersion(currentLanguage),
            items = BibleTranslation.values().toList(),
            selected = displayConfig.bibleTranslation,
            labelProvider = { if (isEn) "${it.labelEn} - ${it.descEn}" else "${it.labelEs} - ${it.descEs}" },
            onSelect = {
                onDisplayConfigChange(displayConfig.copy(bibleTranslation = it))
                showTranslationSheet = false
            },
            onDismiss = { showTranslationSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReaderFormattingSubscreen(
    currentLanguage: AppLanguage,
    displayConfig: ReaderDisplayConfig,
    onDisplayConfigChange: (ReaderDisplayConfig) -> Unit,
    onBack: () -> Unit
) {
    val effectiveLang = resolveLanguage(currentLanguage)
    val isEn = effectiveLang == AppLanguage.ENGLISH
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current

    var showFontSheet by remember { mutableStateOf(false) }
    var showReadingBackgroundSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = if (isEn) "Reader Formatting" else "Formato de Lectura",
                        style = com.jadalai.reinavalera1960.ui.theme.LocalAppFonts.current.topBarTitle,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    FilledTonalIconButton(
                        onClick = onBack,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Text(
                    text = if (isEn) "Typography & Themes" else "Tipografía y Temas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
            }

            // Reading Background
            item {
                androidx.compose.material3.ListItem(
                    modifier = Modifier
                        .clip(com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape(0, 2))
                        .clickable { showReadingBackgroundSheet = true },
                    headlineContent = { Text(AppStrings.readingBackground(currentLanguage), fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text(if (isEn) displayConfig.readingBackgroundTheme.displayNameEn else displayConfig.readingBackgroundTheme.displayNameEs) },
                    leadingContent = {
                        Icon(Icons.Default.FormatPaint, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    trailingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
                )
            }

            // Scripture Font Selection
            item {
                androidx.compose.material3.ListItem(
                    modifier = Modifier
                        .clip(com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape(1, 2))
                        .clickable { showFontSheet = true },
                    headlineContent = { Text(AppStrings.scriptureFont(currentLanguage), fontWeight = FontWeight.SemiBold) },
                    supportingContent = {
                        Text(
                            text = if (isEn) displayConfig.scriptureFont.displayNameEn else displayConfig.scriptureFont.displayNameEs,
                            style = TextStyle(fontFamily = displayConfig.scriptureFont.fontFamily, fontSize = 14.sp)
                        )
                    },
                    leadingContent = {
                        Icon(Icons.Default.FontDownload, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    trailingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (isEn) "Layout & Font Styling" else "Diseño y Estilo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }

            // Text Size Slider Item
            item {
                androidx.compose.material3.ListItem(
                    modifier = Modifier.clip(com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape(0, 3)),
                    headlineContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isEn) "Text size" else "Tamaño del texto",
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${displayConfig.fontSizeSp.toInt()} sp",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    supportingContent = {
                        Slider(
                            value = displayConfig.fontSizeSp,
                            onValueChange = { onDisplayConfigChange(displayConfig.copy(fontSizeSp = it)) },
                            valueRange = 12f..36f,
                            steps = 23,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    colors = com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
                )
            }

            // Text Weight Option Grouped Control
            item {
                androidx.compose.material3.ListItem(
                    modifier = Modifier.clip(com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape(1, 3)),
                    headlineContent = { Text(if (isEn) "Font weight" else "Grosor de fuente", fontWeight = FontWeight.SemiBold) },
                    supportingContent = {
                        com.jadalai.reinavalera1960.ui.components.ConnectedButtonGroup(
                            options = TextWeightOption.values().toList(),
                            selectedOption = displayConfig.textWeight,
                            onOptionSelected = { onDisplayConfigChange(displayConfig.copy(textWeight = it)) },
                            labelProvider = { if (isEn) it.displayNameEn else it.displayNameEs }
                        )
                    },
                    colors = com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
                )
            }

            // Text Justification Option Grouped Control
            item {
                androidx.compose.material3.ListItem(
                    modifier = Modifier.clip(com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape(2, 3)),
                    headlineContent = { Text(if (isEn) "Text alignment" else "Alineación del texto", fontWeight = FontWeight.SemiBold) },
                    supportingContent = {
                        com.jadalai.reinavalera1960.ui.components.ConnectedButtonGroup(
                            options = TextJustification.values().toList(),
                            selectedOption = displayConfig.justification,
                            onOptionSelected = { onDisplayConfigChange(displayConfig.copy(justification = it)) },
                            labelProvider = { if (isEn) it.displayNameEn else it.displayNameEs }
                        )
                    },
                    colors = com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
                )
            }

            // Section 2: Reading & Behavior inside Reader Subscreen
            item {
                Text(
                    text = if (isEn) "Reading and behavior" else "Lectura y comportamiento",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                )
            }

            item {
                ElevatedCard(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Pantalla siempre encendida
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isEn) "Keep screen always on" else "Pantalla siempre encendida",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (isEn) "Screen will never sleep during reading" else "La pantalla nunca se apagará si estás en lectura",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = displayConfig.keepScreenOn,
                                onCheckedChange = { onDisplayConfigChange(displayConfig.copy(keepScreenOn = it)) }
                            )
                        }

                        // Show Highlights
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isEn) "Show verse highlights" else "Mostrar subrayados",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (isEn) "Render highlight colors on verses" else "Mostrar colores en versículos subrayados",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = displayConfig.showHighlights,
                                onCheckedChange = { onDisplayConfigChange(displayConfig.copy(showHighlights = it)) }
                            )
                        }

                        // Show Bookmarks
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isEn) "Show bookmark badges" else "Mostrar marcadores",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (isEn) "Show badge icon on bookmarked verses" else "Mostrar icono en versículos marcados",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = displayConfig.showBookmarks,
                                onCheckedChange = { onDisplayConfigChange(displayConfig.copy(showBookmarks = it)) }
                            )
                        }

                        // Auto-mark as read
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isEn) "Auto-mark as read" else "Marcar como leído automáticamente",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (isEn) "When reaching the end of a chapter, it will be marked as read" else "Al llegar al final de un capítulo, se marcará como leído",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = displayConfig.autoMarkAsRead,
                                onCheckedChange = { onDisplayConfigChange(displayConfig.copy(autoMarkAsRead = it)) }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }

    if (showReadingBackgroundSheet) {
        SelectionSheet(
            title = AppStrings.readingBackground(currentLanguage),
            items = ReadingBackgroundTheme.values().toList(),
            selected = displayConfig.readingBackgroundTheme,
            labelProvider = { if (isEn) it.displayNameEn else it.displayNameEs },
            onSelect = {
                onDisplayConfigChange(displayConfig.copy(readingBackgroundTheme = it))
                showReadingBackgroundSheet = false
            },
            onDismiss = { showReadingBackgroundSheet = false }
        )
    }

    if (showFontSheet) {
        FontSelectionSheet(
            title = AppStrings.scriptureFont(currentLanguage),
            selected = displayConfig.scriptureFont,
            isEn = isEn,
            onSelect = {
                onDisplayConfigChange(displayConfig.copy(scriptureFont = it))
                showFontSheet = false
            },
            onDismiss = { showFontSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutSubscreen(
    currentLanguage: AppLanguage,
    onBack: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val isEn = resolveLanguage(currentLanguage) == AppLanguage.ENGLISH

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = if (isEn) "About" else "Acerca de",
                        style = com.jadalai.reinavalera1960.ui.theme.LocalAppFonts.current.topBarTitle,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    FilledTonalIconButton(
                        onClick = onBack,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Santa Biblia",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Version 1.0.0",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = AppStrings.appDescription(currentLanguage),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Section: Attributions and sources of biblical texts
            item {
                Text(
                    text = AppStrings.attributionsTitle(currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )
            }

            item {
                ElevatedCard(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        // TriGataro / RVR1960 attribution
                        ListItem(
                            headlineContent = {
                                Text("Reina-Valera 1960 (Español)", fontWeight = FontWeight.SemiBold)
                            },
                            supportingContent = {
                                Text("Fuente de datos por TriGataro/Biblia_Reina_Valera_1960 (JSON completo en español)", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            },
                            trailingContent = {
                                FilledTonalButton(
                                    onClick = { uriHandler.openUri("https://github.com/TriGataro/Biblia_Reina_Valera_1960") },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("GitHub")
                                        Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp).padding(start = 4.dp))
                                    }
                                }
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )

                        // farskipper / KJV attribution
                        ListItem(
                            headlineContent = {
                                Text("King James Version 1769 (English)", fontWeight = FontWeight.SemiBold)
                            },
                            supportingContent = {
                                Text("Fuente de datos por farskipper/kjv (Public Domain JSON)", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            },
                            trailingContent = {
                                FilledTonalButton(
                                    onClick = { uriHandler.openUri("https://github.com/farskipper/kjv") },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("GitHub")
                                        Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp).padding(start = 4.dp))
                                    }
                                }
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                    }
                }
            }

            // Section: Developer
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Code,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "@jadalai",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = AppStrings.developerRole(currentLanguage),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))

                        FilledTonalButton(
                            onClick = {
                                uriHandler.openUri("https://github.com/jadalai")
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("GitHub")
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = "External link",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSelectionSheet(
    title: String,
    selected: ScriptureFont,
    isEn: Boolean,
    onSelect: (ScriptureFont) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ScriptureFont.values().forEach { font ->
                val isSelected = font == selected
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                        .clickable { onSelect(font) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isEn) font.displayNameEn else font.displayNameEs,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "En el principio creó Dios los cielos y la tierra.",
                                style = TextStyle(
                                    fontFamily = font.fontFamily,
                                    fontSize = 15.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        if (isSelected) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectionSheet(
    title: String,
    items: List<T>,
    selected: T,
    labelProvider: (T) -> String,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            items.forEach { item ->
                val isSelected = item == selected
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                        .clickable { onSelect(item) }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = labelProvider(item),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
