package com.jadalai.reinavalera1960.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.jadalai.reinavalera1960.ui.theme.ThemeMode

/**
 * M3 Expressive connected grouped control:
 * ButtonGroupDefaults, ToggleButton, and connected button shapes
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T> ConnectedButtonGroup(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    labelProvider: (T) -> String,
    modifier: Modifier = Modifier
) {
    val selectedIndex = options.indexOf(selectedOption)

    Row(
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        options.fastForEachIndexed { index, option ->
            val isSelected = selectedIndex == index
            ToggleButton(
                checked = isSelected,
                onCheckedChange = { onOptionSelected(option) },
                modifier = Modifier
                    .weight(1f)
                    .semantics { role = Role.RadioButton },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                }
            ) {
                Text(
                    text = labelProvider(option),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Expressive theme mode picker
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ThemePickerExpressiveGroup(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    isEn: Boolean,
    modifier: Modifier = Modifier
) {
    val options = listOf(
        Triple(ThemeMode.SYSTEM, Icons.Default.BrightnessAuto, if (isEn) "Auto" else "Auto"),
        Triple(ThemeMode.LIGHT, Icons.Default.LightMode, if (isEn) "Light" else "Claro"),
        Triple(ThemeMode.DARK, Icons.Default.DarkMode, if (isEn) "Dark" else "Oscuro")
    )

    val selectedIndex = options.indexOfFirst { it.first == themeMode }

    Row(
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        options.fastForEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            ToggleButton(
                checked = isSelected,
                onCheckedChange = { onThemeModeChange(item.first) },
                modifier = Modifier
                    .weight(1f)
                    .semantics { role = Role.RadioButton },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(horizontal = 2.dp)
                ) {
                    Icon(
                        imageVector = item.second,
                        contentDescription = null
                    )
                    Text(
                        text = item.third,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
