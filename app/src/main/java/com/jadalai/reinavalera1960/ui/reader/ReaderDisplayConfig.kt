package com.jadalai.reinavalera1960.ui.reader

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.jadalai.reinavalera1960.ui.settings.BibleTranslation
import com.jadalai.reinavalera1960.ui.theme.ReadingBackgroundTheme
import com.jadalai.reinavalera1960.ui.theme.ScriptureFont

enum class TextJustification(val displayNameEs: String, val displayNameEn: String, val align: TextAlign) {
    START("Izquierda", "Left", TextAlign.Start),
    JUSTIFY("Justificado", "Justified", TextAlign.Justify),
    CENTER("Centrado", "Center", TextAlign.Center)
}

enum class TextWeightOption(val displayNameEs: String, val displayNameEn: String, val weight: FontWeight) {
    NORMAL("Normal", "Normal", FontWeight.Normal),
    BOLD("Negrita", "Bold", FontWeight.Bold)
}

data class ReaderDisplayConfig(
    // Tab 1: Appearance
    val readingBackgroundTheme: ReadingBackgroundTheme = ReadingBackgroundTheme.FOLLOW_APP_THEME,
    val fontSizeSp: Float = 18f,
    val brightness: Float = -1f, // -1 means follow system brightness, 0.05f to 1.0f manual override
    val scriptureFont: ScriptureFont = ScriptureFont.LORA,
    val textWeight: TextWeightOption = TextWeightOption.NORMAL,
    val justification: TextJustification = TextJustification.START,
    val separateParagraphs: Boolean = true,

    // Tab 2: Language and version
    val bibleTranslation: BibleTranslation = BibleTranslation.RVR1960,

    // Tab 3: General
    val keepScreenOn: Boolean = false,
    val showHighlights: Boolean = true,
    val showBookmarks: Boolean = true,
    val autoMarkAsRead: Boolean = true
) {
    val fontSize: TextUnit get() = fontSizeSp.sp
    val lineHeight: TextUnit get() = (fontSizeSp * 1.55f).sp
}

val LocalReaderDisplayConfig = compositionLocalOf { ReaderDisplayConfig() }
