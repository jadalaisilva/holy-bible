package com.jadalai.reinavalera1960.ui.components

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jadalai.reinavalera1960.ui.i18n.AppStrings
import com.jadalai.reinavalera1960.ui.i18n.resolveLanguage
import com.jadalai.reinavalera1960.ui.reader.ReaderDisplayConfig
import com.jadalai.reinavalera1960.ui.reader.TextJustification
import com.jadalai.reinavalera1960.ui.reader.TextWeightOption
import com.jadalai.reinavalera1960.ui.search.SearchViewModel
import com.jadalai.reinavalera1960.ui.settings.AppLanguage
import com.jadalai.reinavalera1960.ui.settings.BibleTranslation
import com.jadalai.reinavalera1960.ui.theme.ReadingBackgroundTheme
import com.jadalai.reinavalera1960.ui.theme.ScriptureFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderPreferencesBottomSheet(
    config: ReaderDisplayConfig,
    onConfigChange: (ReaderDisplayConfig) -> Unit,
    currentLanguage: AppLanguage,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val isEn = resolveLanguage(currentLanguage) == AppLanguage.ENGLISH
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Header Segmented Tabs
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val tabs = if (isEn) listOf("Appearance", "Language", "General") else listOf("Apariencia", "Idioma", "General")
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.surface
                                    else Color.Transparent
                                )
                                .clickable { selectedTab = index }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                when (selectedTab) {
                    0 -> AppearanceTabContent(
                        config = config,
                        onConfigChange = onConfigChange,
                        isEn = isEn
                    )
                    1 -> LanguageTabContent(
                        config = config,
                        onConfigChange = onConfigChange,
                        isEn = isEn
                    )
                    2 -> GeneralTabContent(
                        config = config,
                        onConfigChange = onConfigChange,
                        isEn = isEn
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Restore to Default Button
                OutlinedButton(
                    onClick = {
                        onConfigChange(ReaderDisplayConfig())
                        val activity = context as? Activity
                        activity?.window?.attributes = activity?.window?.attributes?.apply {
                            screenBrightness = -1f
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isEn) "Restore to default" else "Restablecer valores por defecto")
                }
            }
        }
    }
}

@Composable
fun AppearanceTabContent(
    config: ReaderDisplayConfig,
    onConfigChange: (ReaderDisplayConfig) -> Unit,
    isEn: Boolean
) {
    val context = LocalContext.current

    // 1. Reading background theme chips
    Column {
        Text(
            text = if (isEn) "Reader theme" else "Tema del lector",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(ReadingBackgroundTheme.values().toList()) { theme ->
                val isSelected = config.readingBackgroundTheme == theme
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                        .clickable { onConfigChange(config.copy(readingBackgroundTheme = theme)) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (isEn) theme.displayNameEn else theme.displayNameEs,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

    // 2. Font size slider
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isEn) "Text size" else "Tamaño del texto",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${config.fontSizeSp.toInt()} sp",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Slider(
            value = config.fontSizeSp,
            onValueChange = { onConfigChange(config.copy(fontSizeSp = it)) },
            valueRange = 12f..32f,
            steps = 9
        )
    }

    // 3. Brightness slider and restore to default brightness button
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isEn) "Brightness" else "Brillo de pantalla",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                FilledTonalIconButton(
                    onClick = {
                        onConfigChange(config.copy(brightness = -1f))
                        val activity = context as? Activity
                        activity?.window?.attributes = activity?.window?.attributes?.apply {
                            screenBrightness = -1f
                        }
                    },
                    modifier = Modifier.size(28.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.BrightnessAuto,
                        contentDescription = "Auto / Restablecer brillo",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = if (config.brightness < 0f) {
                    if (isEn) "Auto (System)" else "Automático (Sistema)"
                } else {
                    "${(config.brightness * 100).toInt()}%"
                },
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Slider(
            value = if (config.brightness < 0f) 0.5f else config.brightness,
            onValueChange = { newBrightness ->
                onConfigChange(config.copy(brightness = newBrightness))
                val activity = context as? Activity
                activity?.window?.attributes = activity?.window?.attributes?.apply {
                    screenBrightness = newBrightness
                }
            },
            valueRange = 0.05f..1f
        )
    }

    // 4. Scripture font selector
    Column {
        Text(
            text = if (isEn) "Typography" else "Tipografía",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(ScriptureFont.values().toList()) { font ->
                val isSelected = config.scriptureFont == font
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                        .clickable { onConfigChange(config.copy(scriptureFont = font)) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (isEn) font.displayNameEn else font.displayNameEs,
                        style = TextStyle(fontFamily = font.fontFamily, fontSize = 14.sp),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

    // 5. Text Weight / Grosor: Single select connected button group
    Column {
        Text(
            text = if (isEn) "Text weight" else "Grosor del texto",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(6.dp))
        ConnectedButtonGroup(
            options = TextWeightOption.values().toList(),
            selectedOption = config.textWeight,
            onOptionSelected = { onConfigChange(config.copy(textWeight = it)) },
            labelProvider = { if (isEn) it.displayNameEn else it.displayNameEs }
        )
    }

    // 6. Text Alignment / Justification: Single select connected button group
    Column {
        Text(
            text = if (isEn) "Text alignment" else "Alineación del texto",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(6.dp))
        ConnectedButtonGroup(
            options = TextJustification.values().toList(),
            selectedOption = config.justification,
            onOptionSelected = { onConfigChange(config.copy(justification = it)) },
            labelProvider = { if (isEn) it.displayNameEn else it.displayNameEs }
        )
    }

    // 7. Separate Paragraphs Switch
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (isEn) "Separate paragraphs" else "Separar párrafos",
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = if (isEn) "Adds comfortable spacing between verses" else "Espaciado cómodo entre versículos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = config.separateParagraphs,
            onCheckedChange = { onConfigChange(config.copy(separateParagraphs = it)) }
        )
    }
}

@Composable
fun LanguageTabContent(
    config: ReaderDisplayConfig,
    onConfigChange: (ReaderDisplayConfig) -> Unit,
    isEn: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = if (isEn) "Bible translation" else "Versión bíblica",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        BibleTranslation.values().forEach { trans ->
            val isSelected = config.bibleTranslation == trans
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onConfigChange(config.copy(bibleTranslation = trans)) }
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            if (isEn) trans.labelEn else trans.labelEs,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    supportingContent = {
                        Text(
                            if (isEn) trans.descEn else trans.descEs,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingContent = {
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun GeneralTabContent(
    config: ReaderDisplayConfig,
    onConfigChange: (ReaderDisplayConfig) -> Unit,
    isEn: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Screen always on
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isEn) "Keep screen always on" else "Pantalla siempre encendida",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (isEn) "Screen will not sleep while reading" else "La pantalla nunca se apagará durante la lectura",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = config.keepScreenOn,
                onCheckedChange = { onConfigChange(config.copy(keepScreenOn = it)) }
            )
        }

        // Show highlights
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isEn) "Show verse highlights" else "Mostrar subrayados",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (isEn) "Render colored highlights in text" else "Resaltar versículos subrayados con color",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = config.showHighlights,
                onCheckedChange = { onConfigChange(config.copy(showHighlights = it)) }
            )
        }

        // Show bookmarks
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isEn) "Show bookmark badges" else "Mostrar marcadores",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (isEn) "Show bookmark icons on saved verses" else "Mostrar icono de marcador en versículos guardados",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = config.showBookmarks,
                onCheckedChange = { onConfigChange(config.copy(showBookmarks = it)) }
            )
        }

        // Mark as read automatically
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(horizontal = 16.dp, vertical = 12.dp),
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
                checked = config.autoMarkAsRead,
                onCheckedChange = { onConfigChange(config.copy(autoMarkAsRead = it)) }
            )
        }
    }
}


