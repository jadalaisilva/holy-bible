package com.jadalai.reinavalera1960.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.jadalai.reinavalera1960.ui.reader.ReaderDisplayConfig
import com.jadalai.reinavalera1960.ui.reader.TextJustification
import com.jadalai.reinavalera1960.ui.reader.TextWeightOption
import com.jadalai.reinavalera1960.ui.settings.AppLanguage
import com.jadalai.reinavalera1960.ui.settings.BibleTranslation
import com.jadalai.reinavalera1960.ui.theme.ColorPreset
import com.jadalai.reinavalera1960.ui.theme.ReadingBackgroundTheme
import com.jadalai.reinavalera1960.ui.theme.ScriptureFont
import com.jadalai.reinavalera1960.ui.theme.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_settings_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "key_onboarding_completed"
        private const val KEY_THEME_MODE = "key_theme_mode"
        private const val KEY_COLOR_PRESET = "key_color_preset"
        private const val KEY_SEED_COLOR = "key_seed_color"
        private const val KEY_DYNAMIC_COLOR = "key_dynamic_color"
        private const val KEY_BLACK_THEME = "key_black_theme"
        private const val KEY_APP_LANGUAGE = "key_app_language"

        // ReaderDisplayConfig keys
        private const val KEY_READING_BG_THEME = "key_reading_bg_theme"
        private const val KEY_FONT_SIZE = "key_font_size"
        private const val KEY_BRIGHTNESS = "key_brightness"
        private const val KEY_SCRIPTURE_FONT = "key_scripture_font"
        private const val KEY_TEXT_WEIGHT = "key_text_weight"
        private const val KEY_JUSTIFICATION = "key_justification"
        private const val KEY_SEPARATE_PARAGRAPHS = "key_separate_paragraphs"
        private const val KEY_BIBLE_TRANSLATION = "key_bible_translation"
        private const val KEY_KEEP_SCREEN_ON = "key_keep_screen_on"
        private const val KEY_SHOW_HIGHLIGHTS = "key_show_highlights"
        private const val KEY_SHOW_BOOKMARKS = "key_show_bookmarks"
        private const val KEY_SHOW_FAVORITES = "key_show_favorites"
        private const val KEY_AUTO_MARK_AS_READ = "key_auto_mark_as_read"

        // Last read position
        private const val KEY_LAST_BOOK_ID = "key_last_book_id"
        private const val KEY_LAST_CHAPTER_NUM = "key_last_chapter_num"
    }

    fun isOnboardingCompleted(): Boolean = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    fun setOnboardingCompleted(completed: Boolean = true) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }

    fun getThemeMode(): ThemeMode {
        val name = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        return try { ThemeMode.valueOf(name) } catch (e: Exception) { ThemeMode.SYSTEM }
    }

    fun saveThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
    }

    fun getColorPreset(): ColorPreset {
        val name = prefs.getString(KEY_COLOR_PRESET, ColorPreset.DYNAMIC.name) ?: ColorPreset.DYNAMIC.name
        return try { ColorPreset.valueOf(name) } catch (e: Exception) { ColorPreset.DYNAMIC }
    }

    fun saveColorPreset(preset: ColorPreset) {
        prefs.edit().putString(KEY_COLOR_PRESET, preset.name).apply()
    }

    fun isDynamicColor(): Boolean = prefs.getBoolean(KEY_DYNAMIC_COLOR, true)

    fun saveDynamicColor(dynamic: Boolean) {
        prefs.edit().putBoolean(KEY_DYNAMIC_COLOR, dynamic).apply()
    }

    fun isBlackTheme(): Boolean = prefs.getBoolean(KEY_BLACK_THEME, false)

    fun saveBlackTheme(black: Boolean) {
        prefs.edit().putBoolean(KEY_BLACK_THEME, black).apply()
    }

    fun getSeedColorHex(): Long = prefs.getLong(KEY_SEED_COLOR, 0xFF644ABBL)

    fun saveSeedColorHex(colorLong: Long) {
        prefs.edit().putLong(KEY_SEED_COLOR, colorLong).apply()
    }

    fun getAppLanguage(): AppLanguage {
        val name = prefs.getString(KEY_APP_LANGUAGE, AppLanguage.SYSTEM.name) ?: AppLanguage.SYSTEM.name
        return try { AppLanguage.valueOf(name) } catch (e: Exception) { AppLanguage.SYSTEM }
    }

    fun saveAppLanguage(lang: AppLanguage) {
        prefs.edit().putString(KEY_APP_LANGUAGE, lang.name).apply()
    }

    fun getReaderDisplayConfig(): ReaderDisplayConfig {
        val bgThemeName = prefs.getString(KEY_READING_BG_THEME, ReadingBackgroundTheme.FOLLOW_APP_THEME.name)
        val fontName = prefs.getString(KEY_SCRIPTURE_FONT, ScriptureFont.LORA.name)
        val weightName = prefs.getString(KEY_TEXT_WEIGHT, TextWeightOption.NORMAL.name)
        val justName = prefs.getString(KEY_JUSTIFICATION, TextJustification.START.name)
        val transName = prefs.getString(KEY_BIBLE_TRANSLATION, BibleTranslation.RVR1960.name)

        return ReaderDisplayConfig(
            readingBackgroundTheme = try { ReadingBackgroundTheme.valueOf(bgThemeName ?: "") } catch (e: Exception) { ReadingBackgroundTheme.FOLLOW_APP_THEME },
            fontSizeSp = prefs.getFloat(KEY_FONT_SIZE, 18f),
            brightness = prefs.getFloat(KEY_BRIGHTNESS, -1f),
            scriptureFont = try { ScriptureFont.valueOf(fontName ?: "") } catch (e: Exception) { ScriptureFont.LORA },
            textWeight = try { TextWeightOption.valueOf(weightName ?: "") } catch (e: Exception) { TextWeightOption.NORMAL },
            justification = try { TextJustification.valueOf(justName ?: "") } catch (e: Exception) { TextJustification.START },
            separateParagraphs = prefs.getBoolean(KEY_SEPARATE_PARAGRAPHS, true),
            bibleTranslation = try { BibleTranslation.valueOf(transName ?: "") } catch (e: Exception) { BibleTranslation.RVR1960 },
            keepScreenOn = prefs.getBoolean(KEY_KEEP_SCREEN_ON, false),
            showHighlights = prefs.getBoolean(KEY_SHOW_HIGHLIGHTS, true),
            showBookmarks = prefs.getBoolean(KEY_SHOW_BOOKMARKS, true),
            autoMarkAsRead = prefs.getBoolean(KEY_AUTO_MARK_AS_READ, true)
        )
    }

    fun saveReaderDisplayConfig(config: ReaderDisplayConfig) {
        prefs.edit()
            .putString(KEY_READING_BG_THEME, config.readingBackgroundTheme.name)
            .putFloat(KEY_FONT_SIZE, config.fontSizeSp)
            .putFloat(KEY_BRIGHTNESS, config.brightness)
            .putString(KEY_SCRIPTURE_FONT, config.scriptureFont.name)
            .putString(KEY_TEXT_WEIGHT, config.textWeight.name)
            .putString(KEY_JUSTIFICATION, config.justification.name)
            .putBoolean(KEY_SEPARATE_PARAGRAPHS, config.separateParagraphs)
            .putString(KEY_BIBLE_TRANSLATION, config.bibleTranslation.name)
            .putBoolean(KEY_KEEP_SCREEN_ON, config.keepScreenOn)
            .putBoolean(KEY_SHOW_HIGHLIGHTS, config.showHighlights)
            .putBoolean(KEY_SHOW_BOOKMARKS, config.showBookmarks)
            .putBoolean(KEY_AUTO_MARK_AS_READ, config.autoMarkAsRead)
            .apply()
    }

    fun getLastReadBookId(): Int = prefs.getInt(KEY_LAST_BOOK_ID, 1)
    fun getLastReadChapterNum(): Int = prefs.getInt(KEY_LAST_CHAPTER_NUM, 1)

    fun saveLastReadPosition(bookId: Int, chapterNum: Int) {
        prefs.edit()
            .putInt(KEY_LAST_BOOK_ID, bookId)
            .putInt(KEY_LAST_CHAPTER_NUM, chapterNum)
            .apply()
    }
}
