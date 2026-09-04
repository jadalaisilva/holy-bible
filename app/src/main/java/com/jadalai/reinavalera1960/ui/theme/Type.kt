@file:OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)

package com.jadalai.reinavalera1960.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jadalai.reinavalera1960.R

// Top bar titles font
val GoogleSansFlexTopBarFont = FontFamily(
    Font(
        R.font.google_sans_flex,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(900),
            FontVariation.width(112.5f),
            FontVariation.Setting("ROND", 35f)
        )
    )
)

// Rounded display and headline font
val GoogleSansFlexRond100W600 = FontFamily(
    Font(
        R.font.google_sans_flex,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(600),
            FontVariation.Setting("ROND", 100f)
        )
    )
)

// Regular font
val GoogleSansFlexRegular = FontFamily(
    Font(
        R.font.google_sans_flex,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400)
        )
    )
)

// General font family
val GoogleFlexFontFamily = FontFamily(
    Font(R.font.google_sans_flex, weight = FontWeight.Light),
    Font(R.font.google_sans_flex, weight = FontWeight.Normal),
    Font(R.font.google_sans_flex, weight = FontWeight.Medium),
    Font(R.font.google_sans_flex, weight = FontWeight.SemiBold),
    Font(R.font.google_sans_flex, weight = FontWeight.Bold),
    Font(R.font.google_sans_flex, weight = FontWeight.ExtraBold),
    Font(R.font.google_sans_flex, weight = FontWeight.Black)
)

// Reading font families with Normal and Bold weight support
val LoraFontFamily = FontFamily(
    Font(R.font.lora, weight = FontWeight.Normal),
    Font(R.font.lora, weight = FontWeight.Bold)
)
val RobotoSlabFontFamily = FontFamily(
    Font(R.font.roboto_slab, weight = FontWeight.Normal),
    Font(R.font.roboto_slab, weight = FontWeight.Bold)
)
val OpenSansFontFamily = FontFamily(
    Font(R.font.open_sans, weight = FontWeight.Normal),
    Font(R.font.open_sans, weight = FontWeight.Bold)
)
val RalewayFontFamily = FontFamily(
    Font(R.font.raleway, weight = FontWeight.Normal),
    Font(R.font.raleway, weight = FontWeight.Bold)
)
val LiterataFontFamily = FontFamily(
    Font(R.font.literata, weight = FontWeight.Normal),
    Font(R.font.literata, weight = FontWeight.Bold)
)
val VollkornFontFamily = FontFamily(
    Font(R.font.vollkorn, weight = FontWeight.Normal),
    Font(R.font.vollkorn, weight = FontWeight.Bold)
)
val AtkinsonHyperlegibleFontFamily = FontFamily(
    Font(R.font.atkinson_hyperlegible, weight = FontWeight.Normal),
    Font(R.font.atkinson_hyperlegible, weight = FontWeight.Bold)
)
val InterFontFamily = FontFamily(
    Font(R.font.inter, weight = FontWeight.Normal),
    Font(R.font.inter, weight = FontWeight.Bold)
)

// Top bar titles font style configuration
val TopBarTitleStyle = TextStyle(
    fontFamily = GoogleSansFlexTopBarFont,
    fontWeight = FontWeight.Black,
    fontSize = 32.sp,
    lineHeight = 38.sp,
    letterSpacing = (-0.5).sp
)

data class AppFonts(
    val topBarTitle: TextStyle = TopBarTitleStyle,
    val annotatedNormal: TextStyle = TextStyle(
        fontFamily = GoogleSansFlexRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    val annotatedBold: TextStyle = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )
)

val LocalAppFonts = compositionLocalOf { AppFonts() }

val Typography = Typography(
    // Display styles
    displayLarge = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontFeatureSettings = "ss02, dlig",
        fontSize = 56.sp,
        lineHeight = 60.sp,
        letterSpacing = (-1.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontFeatureSettings = "ss02, dlig",
        fontSize = 44.sp,
        lineHeight = 48.sp,
        letterSpacing = (-1.0).sp
    ),
    displaySmall = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontFeatureSettings = "ss02, dlig",
        fontSize = 36.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.8).sp
    ),

    // Headline styles
    headlineLarge = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontFeatureSettings = "ss02, dlig",
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.75).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontFeatureSettings = "ss02, dlig",
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontFeatureSettings = "ss02, dlig",
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.3).sp
    ),

    // Titles
    titleLarge = TextStyle(
        fontFamily = GoogleSansFlexRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.25).sp
    ),
    titleMedium = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.15).sp
    ),
    titleSmall = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    // Body text
    bodyLarge = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.1).sp
    ),
    bodyMedium = TextStyle(
        fontFamily = GoogleSansFlexRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = GoogleSansFlexRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp
    ),

    // Labels and buttons
    labelLarge = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontFeatureSettings = "ss02, dlig",
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontFeatureSettings = "ss02, dlig",
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp
    ),
    labelSmall = TextStyle(
        fontFamily = GoogleSansFlexRond100W600,
        fontWeight = FontWeight.SemiBold,
        fontFeatureSettings = "ss02, dlig",
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.3.sp
    )
)
