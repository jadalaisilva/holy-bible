package com.jadalai.reinavalera1960.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import com.materialkolor.rememberDynamicColorScheme

enum class ThemeMode(val displayNameEs: String, val displayNameEn: String) {
    SYSTEM("Sistema", "System"),
    DARK("Oscuro", "Dark"),
    LIGHT("Claro", "Light")
}

enum class ColorPreset(
    val displayNameEs: String,
    val displayNameEn: String,
    val seedColor: Color
) {
    DYNAMIC("Dínamico", "Dynamic", Color(0xFF644ABB)),
    PEACH("Melocotón", "Peach", Color(0xFFFEB4A7)),
    ROSE("Rosa", "Rose", Color(0xFFFFB3C0)),
    MAGENTA("Magenta", "Magenta", Color(0xFFFCAAFF)),
    LAVENDER("Lavanda", "Lavender", Color(0xFFB9C3FF)),
    SKY("Cielo", "Sky", Color(0xFF62D3FF)),
    CYAN("Cian", "Cyan", Color(0xFF44D9F1)),
    TEAL("Verde azulado", "Teal", Color(0xFF52DBC9)),
    MINT("Menta", "Mint", Color(0xFF78DD77)),
    LIME("Lima", "Lime", Color(0xFF9FD75C)),
    AMBER("Ámbar", "Amber", Color(0xFFFABD00)),
    ORANGE("Naranja", "Orange", Color(0xFFFFB86E)),
    SEPIA("Sepia", "Sepia", Color(0xFF795548)),
    MONOCHROME("Monocromo", "Monochrome", Color(0xFF757575))
}

enum class ScriptureFont(val displayNameEs: String, val displayNameEn: String, val fontFamily: FontFamily) {
    LORA("Lora", "Lora", LoraFontFamily),
    ROBOTO_SLAB("Roboto Slab", "Roboto Slab", RobotoSlabFontFamily),
    LITERATA("Literata", "Literata", LiterataFontFamily),
    VOLLKORN("Vollkorn", "Vollkorn", VollkornFontFamily),
    ATKINSON_HYPERLEGIBLE("Atkinson Hyperlegible", "Atkinson Hyperlegible", AtkinsonHyperlegibleFontFamily),
    OPEN_SANS("Open Sans", "Open Sans", OpenSansFontFamily),
    INTER("Inter", "Inter", InterFontFamily),
    RALEWAY("Raleway", "Raleway", RalewayFontFamily),
    GOOGLE_SANS_FLEX("Google Sans Flex", "Google Sans Flex", GoogleFlexFontFamily)
}

enum class ReadingBackgroundTheme(
    val displayNameEs: String,
    val displayNameEn: String,
    val backgroundColor: Color?,
    val textColor: Color?
) {
    FOLLOW_APP_THEME("Seguir tema de la app", "Follow app theme", null, null),
    PURE_WHITE("Blanco", "White", Color(0xFFFFFFFF), Color(0xFF111111)),
    PURE_BLACK("Negro", "Black", Color(0xFF000000), Color(0xFFFFFFFF)),
    WARM_SEPIA("Sepia", "Sepia", Color(0xFFFBF0D9), Color(0xFF3E2723)),
    NIGHT_CHARCOAL("Carbón", "Charcoal", Color(0xFF1E1E1E), Color(0xFFE0E0E0))
}

val LocalScriptureFont = compositionLocalOf { ScriptureFont.LORA }
val LocalReadingBackgroundTheme = compositionLocalOf { ReadingBackgroundTheme.FOLLOW_APP_THEME }

private val DefaultDarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    onPrimary = Color(0xFF381E72),
    onSecondary = Color(0xFF332D41),
    onTertiary = Color(0xFF492532),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    onSurfaceVariant = Color(0xFFCAC4D0)
)

private val DefaultLightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = LightBackground,
    surface = LightBackground,
    surfaceVariant = LightSurfaceVariant,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1E1627),
    onSurface = Color(0xFF1E1627),
    onSurfaceVariant = Color(0xFF4C4456)
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BibleAppTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    seedColor: Color = Color.White,
    dynamicColor: Boolean = true,
    blackTheme: Boolean = false,
    scriptureFont: ScriptureFont = ScriptureFont.LORA,
    readingBackgroundTheme: ReadingBackgroundTheme = ReadingBackgroundTheme.FOLLOW_APP_THEME,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemDark
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }

    val context = LocalContext.current
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val systemDynamic = if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        if (blackTheme && isDark) {
            systemDynamic.copy(
                background = Color.Black,
                surface = Color.Black,
                surfaceContainer = Color(0xFF121212),
                surfaceContainerHigh = Color(0xFF1E1E1E)
            )
        } else {
            systemDynamic
        }
    } else {
        rememberDynamicColorScheme(
            seedColor = seedColor,
            isDark = isDark,
            isAmoled = blackTheme && isDark,
            style = com.materialkolor.PaletteStyle.Vibrant
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !isDark
                insetsController.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    CompositionLocalProvider(
        LocalAppFonts provides AppFonts(),
        LocalScriptureFont provides scriptureFont,
        LocalReadingBackgroundTheme provides readingBackgroundTheme
    ) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            typography = Typography,
            motionScheme = MotionScheme.expressive(),
            content = content
        )
    }
}

