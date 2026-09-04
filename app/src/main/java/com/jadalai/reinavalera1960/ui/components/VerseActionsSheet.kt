package com.jadalai.reinavalera1960.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FormatColorReset
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jadalai.reinavalera1960.data.local.dao.VerseWithDetails
import com.jadalai.reinavalera1960.ui.theme.HighlightBlue
import com.jadalai.reinavalera1960.ui.theme.HighlightGreen
import com.jadalai.reinavalera1960.ui.theme.HighlightOrange
import com.jadalai.reinavalera1960.ui.theme.HighlightPurple
import com.jadalai.reinavalera1960.ui.theme.HighlightYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerseActionsSheet(
    verses: List<VerseWithDetails>,
    isEnglish: Boolean = false,
    onDismiss: () -> Unit,
    onHighlight: (String?) -> Unit,
    onBookmark: (String?) -> Unit,
    onShare: () -> Unit,
    onPlayAudio: () -> Unit
) {
    if (verses.isEmpty()) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showNoteDialog by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }

    val highlightColors = listOf(
        "#FFF59D" to HighlightYellow,
        "#A5D6A7" to HighlightGreen,
        "#90CAF9" to HighlightBlue,
        "#FFCCBC" to HighlightOrange,
        "#CE93D8" to HighlightPurple
    )

    val firstVerse = verses.first()
    val isMulti = verses.size > 1
    val localizedBook = com.jadalai.reinavalera1960.ui.i18n.AppStrings.getLocalizedBookName(firstVerse.book_id, firstVerse.book_name, isEnglish)
    val headerTitle = if (isMulti) {
        val lastVerse = verses.last()
        if (isEnglish) {
            "$localizedBook ${firstVerse.chapter_number}:${firstVerse.verse_number}-${lastVerse.verse_number} (${verses.size} verses)"
        } else {
            "$localizedBook ${firstVerse.chapter_number}:${firstVerse.verse_number}-${lastVerse.verse_number} (${verses.size} versículos)"
        }
    } else {
        "$localizedBook ${firstVerse.chapter_number}:${firstVerse.verse_number}"
    }

    val headerContent = if (isMulti) {
        verses.joinToString(" ") { "${it.verse_number}. ${it.content_text}" }
    } else {
        "\"${firstVerse.content_text}\""
    }

    val currentHighlightColor = if (verses.all { it.highlight_color == firstVerse.highlight_color }) {
        firstVerse.highlight_color
    } else {
        null
    }

    val allBookmarked = verses.all { it.is_bookmarked }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Verse Header
            Text(
                text = headerTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = headerContent,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Highlight Color Picker
            Text(
                text = "Subrayar:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isNoneSelected = currentHighlightColor.isNullOrBlank()
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isNoneSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                        .border(
                            width = if (isNoneSelected) 2.dp else 1.dp,
                            color = if (isNoneSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                        .clickable { onHighlight(null) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatColorReset,
                        contentDescription = "Sin subrayar",
                        tint = if (isNoneSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                highlightColors.forEach { (hex, color) ->
                    val isSelected = currentHighlightColor.equals(hex, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                                shape = CircleShape
                            )
                            .clickable { onHighlight(hex) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showNoteDialog) {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Nota personalizada (Opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showNoteDialog = false }) {
                        Text("Cancelar")
                    }
                    Button(onClick = {
                        onBookmark(noteText.ifBlank { null })
                        showNoteDialog = false
                    }) {
                        Text(if (isMulti) "Guardar Marcador (${verses.size} versículos)" else "Guardar Marcador")
                    }
                }
            } else {
                // Bookmark Action
                ListItem(
                    headlineContent = {
                        Text(
                            if (allBookmarked) {
                                if (isMulti) "Quitar de marcadores (${verses.size})" else "Quitar de marcadores"
                            } else {
                                if (isMulti) "Guardar marcadores (${verses.size})" else "Guardar en marcadores"
                            }
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = if (allBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable {
                        if (allBookmarked) {
                            onBookmark(null)
                        } else {
                            showNoteDialog = true
                        }
                    }
                )

                // Share Action
                ListItem(
                    headlineContent = {
                        Text(if (isMulti) "Compartir versículos (${verses.size})" else "Compartir versículo")
                    },
                    leadingContent = { Icon(imageVector = Icons.Default.Share, contentDescription = null) },
                    modifier = Modifier.clickable { onShare() }
                )

                // Audio Action
                ListItem(
                    headlineContent = { Text("Escuchar audio del capítulo") },
                    leadingContent = { Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null) },
                    modifier = Modifier.clickable { onPlayAudio() }
                )
            }
        }
    }
}
