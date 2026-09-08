package com.jadalai.reinavalera1960.ui.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light default palette
val Purple40 = Color(0xFF644ABB)
val PurpleGrey40 = Color(0xFF625B71)
val Pink40 = Color(0xFF7D5260)
val LightBackground = Color(0xFFECE4F5)
val LightSurface = Color(0xFFF7F2FA)
val LightSurfaceVariant = Color(0xFFE5DBF0)
val LightSurfaceContainer = Color(0xFFECE4F5)
val LightSurfaceContainerHigh = Color(0xFFDFD4EC)

// Dark default palette
val Purple80 = Color(0xFFD1BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val DarkBackground = Color(0xFF1B142A)
val DarkSurface = Color(0xFF2E2448)
val DarkSurfaceContainer = Color(0xFF1B142A)
val DarkSurfaceContainerHigh = Color(0xFF382C56)
val DarkSurfaceVariant = Color(0xFF483D60)

// Verse Highlighting Standard Colors
val HighlightYellow = Color(0xFFFFF59D)
val HighlightGreen = Color(0xFFA5D6A7)
val HighlightBlue = Color(0xFF90CAF9)
val HighlightOrange = Color(0xFFFFCCBC)
val HighlightPurple = Color(0xFFCE93D8)
val HighlightNone = Color.Transparent

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
object CustomColors {
    var black = false

    val topBarColors: TopAppBarColors
        @Composable get() =
            TopAppBarDefaults.topAppBarColors(
                containerColor = if (!black) colorScheme.surfaceContainer else colorScheme.surface,
                scrolledContainerColor = if (!black) colorScheme.surfaceContainer else colorScheme.surface
            )

    val detailPaneTopBarColors: TopAppBarColors
        @Composable get() =
            TopAppBarDefaults.topAppBarColors(
                containerColor = if (!black) colorScheme.surfaceContainerLow else colorScheme.surface,
                scrolledContainerColor = if (!black) colorScheme.surfaceContainerLow else colorScheme.surface
            )

    val listItemColors: ListItemColors
        @Composable get() =
            ListItemDefaults.colors(
                containerColor = if (!black) colorScheme.surfaceContainerHigh else colorScheme.surfaceContainer
            )

    val switchColors: SwitchColors
        @Composable get() = SwitchDefaults.colors(
            checkedIconColor = colorScheme.primary,
        )
}

