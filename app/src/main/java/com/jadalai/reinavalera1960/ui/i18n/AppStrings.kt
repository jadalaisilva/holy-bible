package com.jadalai.reinavalera1960.ui.i18n

import com.jadalai.reinavalera1960.ui.settings.AppLanguage
import java.util.Locale

fun resolveLanguage(language: AppLanguage): AppLanguage {
    return when (language) {
        AppLanguage.SYSTEM -> {
            val systemLang = Locale.getDefault().language.lowercase()
            if (systemLang.startsWith("en")) AppLanguage.ENGLISH else AppLanguage.SPANISH
        }
        AppLanguage.SPANISH -> AppLanguage.SPANISH
        AppLanguage.ENGLISH -> AppLanguage.ENGLISH
    }
}

object AppStrings {
    fun navGuide(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Guide"
        else -> "Guía"
    }

    fun navRead(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Read"
        else -> "Lectura"
    }

    fun navSearch(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Search"
        else -> "Buscar"
    }

    fun navBookmarks(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Bookmarks"
        else -> "Marcadores"
    }

    fun navSettings(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Settings"
        else -> "Configuración"
    }

    fun topBarReadTitle(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Holy Bible"
        else -> "Santa Biblia"
    }

    fun topBarSearchTitle(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Search"
        else -> "Búsqueda"
    }

    fun topBarBookmarksTitle(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Bookmarks"
        else -> "Marcadores"
    }

    fun topBarFavoritesTitle(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Favorites"
        else -> "Favoritos"
    }

    fun topBarSettingsTitle(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Settings"
        else -> "Configuración"
    }

    fun searchPlaceholder(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Search..."
        else -> "Buscar..."
    }

    fun searchSuggestionsTitle(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Suggestions"
        else -> "Sugerencias"
    }

    fun filterAll(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Whole Bible"
        else -> "Toda la Biblia"
    }

    fun filterOT(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Old Testament"
        else -> "Antiguo Testamento"
    }

    fun filterNT(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "New Testament"
        else -> "Nuevo Testamento"
    }

    fun emptyBookmarks(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "No saved bookmarks yet."
        else -> "Sin marcadores guardados aún."
    }

    fun emptyFavorites(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "No favorite verses yet.\nSelect a verse in the reader and tap \"Mark as Favorite\"."
        else -> "Sin versículos favoritos aún.\nSeleccione un versículo en el lector y pulse \"Marcar como Favorito\"."
    }

    fun previousChapter(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> " Previous"
        else -> " Anterior"
    }

    fun nextChapter(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Next "
        else -> "Siguiente "
    }

    fun highlight(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Highlight:"
        else -> "Subrayar:"
    }

    fun shareVerse(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Share verse"
        else -> "Compartir versículo"
    }

    fun listenChapterAudio(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Listen to chapter audio"
        else -> "Escuchar audio del capítulo"
    }

    fun readVerseTts(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Read out loud (Voice TTS)"
        else -> "Leer en voz alta (Voz TTS)"
    }

    fun bookmarkRemoved(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Bookmark removed"
        else -> "Marcador eliminado"
    }

    fun favoriteRemoved(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Removed from favorites"
        else -> "Eliminado de favoritos"
    }

    fun undo(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Undo"
        else -> "Deshacer"
    }

    fun appearanceSection(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Appearance"
        else -> "Apariencia"
    }

    fun appTheme(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "App theme"
        else -> "Tema de la app"
    }

    fun colorPreset(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Color Scheme"
        else -> "Esquema de colores"
    }

    fun readingBackground(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Background and text"
        else -> "Fondo y texto"
    }

    fun scriptureFont(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Text typography"
        else -> "Tipografía del texto"
    }

    fun languageSection(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Language"
        else -> "Idioma"
    }

    fun appLanguage(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "App language"
        else -> "Idioma de la aplicación"
    }

    fun bibleVersion(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Bible version"
        else -> "Versión bíblica"
    }

    fun pureBlackTheme(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Pure black theme"
        else -> "Tema negro puro"
    }

    fun pureBlackThemeDesc(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Use a pure black background color on dark theme"
        else -> "Use un color de fondo negro puro en modo oscuro"
    }

    fun typographyAndThemes(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Typography and themes"
        else -> "Tipografía y temas"
    }

    fun layoutAndFontStyling(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Layout and font styling"
        else -> "Diseño y estilo"
    }

    fun readingAndBehavior(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Reading and behavior"
        else -> "Lectura y comportamiento"
    }

    fun keepScreenOn(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Keep screen always on"
        else -> "Pantalla siempre encendida"
    }

    fun keepScreenOnDesc(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Screen will never sleep during reading"
        else -> "La pantalla nunca se apagará si estás en lectura"
    }

    fun showVerseHighlights(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Show verse highlights"
        else -> "Mostrar subrayados"
    }

    fun showVerseHighlightsDesc(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Render highlight colors on verses"
        else -> "Mostrar colores en versículos subrayados"
    }

    fun showBookmarkBadges(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Show bookmark badges"
        else -> "Mostrar marcadores"
    }

    fun showBookmarkBadgesDesc(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Show badge icon on bookmarked verses"
        else -> "Mostrar icono en versículos marcados"
    }

    fun autoMarkAsRead(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Auto-mark as read"
        else -> "Marcar como leído automáticamente"
    }

    fun autoMarkAsReadDesc(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "When reaching the end of a chapter, it will be marked as read"
        else -> "Al llegar al final de un capítulo, se marcará como leído"
    }

    fun readerFormattingTitle(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Reader Formatting"
        else -> "Formato de Lectura"
    }

    fun theme(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Theme"
        else -> "Tema"
    }

    fun themeSystem(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "System"
        else -> "Sistema"
    }

    fun themeLight(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Light"
        else -> "Claro"
    }

    fun themeDark(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Dark"
        else -> "Oscuro"
    }

    fun aboutSection(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "About"
        else -> "Acerca de"
    }

    fun attributionsTitle(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Attributions"
        else -> "Atribuciones"
    }

    fun developerRole(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Developer"
        else -> "Desarrollador"
    }

    fun appDescription(lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "Holy Bible app with Reina-Valera 1960 and King James Version (KJV 1769)."
        else -> "Aplicación de la Santa Biblia con Reina-Valera 1960 y King James Version (KJV 1769)."
    }

    fun searchResultsFound(count: Int, lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "$count verses found"
        else -> "$count versículos encontrados"
    }

    fun noResultsFound(query: String, lang: AppLanguage) = when (resolveLanguage(lang)) {
        AppLanguage.ENGLISH -> "No verses found for \"$query\""
        else -> "No se encontraron versículos para \"$query\""
    }

    val englishBookNames = listOf(
        "Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",
        "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel",
        "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra",
        "Nehemiah", "Esther", "Job", "Psalms", "Proverbs",
        "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations",
        "Ezekiel", "Daniel", "Hosea", "Joel", "Amos",
        "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk",
        "Zephaniah", "Haggai", "Zechariah", "Malachi",
        "Matthew", "Mark", "Luke", "John", "Acts",
        "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians",
        "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy",
        "2 Timothy", "Titus", "Philemon", "Hebrews", "James",
        "1 Peter", "2 Peter", "1 John", "2 John", "3 John",
        "Jude", "Revelation"
    )

    fun getLocalizedBookName(bookId: Int, spanishName: String, isEnglish: Boolean): String {
        if (isEnglish && bookId in 1..englishBookNames.size) {
            return englishBookNames[bookId - 1]
        }
        return spanishName
    }
}
