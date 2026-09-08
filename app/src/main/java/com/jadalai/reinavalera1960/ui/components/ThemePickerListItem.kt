package com.jadalai.reinavalera1960.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape
import com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
import com.jadalai.reinavalera1960.ui.theme.ThemeMode

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ThemePickerListItem(
    themeMode: ThemeMode,
    items: Int,
    index: Int,
    isEn: Boolean,
    onThemeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val themeMap: Map<ThemeMode, Pair<ImageVector, String>> = remember(isEn) {
        mapOf(
            ThemeMode.SYSTEM to Pair(
                Icons.Default.BrightnessAuto,
                if (isEn) "System" else "Sistema"
            ),
            ThemeMode.LIGHT to Pair(
                Icons.Default.LightMode,
                if (isEn) "Light" else "Claro"
            ),
            ThemeMode.DARK to Pair(
                Icons.Default.DarkMode,
                if (isEn) "Dark" else "Oscuro"
            )
        )
    }

    ListItem(
        leadingContent = {
            AnimatedContent(themeMap[themeMode]!!.first, label = "themeIcon") { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
            }
        },
        headlineContent = { Text(if (isEn) "Theme" else "Tema") },
        supportingContent = {
            val options = themeMap.toList()
            val selectedIndex = options.indexOfFirst { it.first == themeMode }

            Row(
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                options.fastForEachIndexed { optIndex, themeEntry ->
                    val isSelected = selectedIndex == optIndex
                    ToggleButton(
                        checked = isSelected,
                        onCheckedChange = { onThemeChange(themeEntry.first) },
                        modifier = Modifier
                            .weight(1f)
                            .semantics { role = Role.RadioButton },
                        shapes = when (optIndex) {
                            0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                            options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                        },
                    ) {
                        Text(
                            text = themeEntry.second.second,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        },
        colors = listItemColors,
        modifier = modifier.clip(segmentedListItemShape(index, items))
    )
}
