package com.jadalai.reinavalera1960.ui.components

import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jadalai.reinavalera1960.ui.theme.BibleShapeDefaults.segmentedListItemShape
import com.jadalai.reinavalera1960.ui.theme.CustomColors.listItemColors
import com.jadalai.reinavalera1960.ui.theme.CustomColors.switchColors

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ColorSchemePickerListItem(
    currentColor: Color,
    dynamicColor: Boolean,
    items: Int,
    index: Int,
    isEn: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorSchemes = listOf(
        Color(0xfffeb4a7), Color(0xffffb3c0), Color(0xfffcaaff), Color(0xffb9c3ff),
        Color(0xff62d3ff), Color(0xff44d9f1), Color(0xff52dbc9), Color(0xff78dd77),
        Color(0xff9fd75c), Color(0xffc1d02d), Color(0xfffabd00), Color(0xffffb86e),
        Color(0xFF795548), Color(0xFF757575)
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ListItem(
            headlineContent = { Text(if (isEn) "Dynamic color" else "Color dinámico") },
            supportingContent = { Text(if (isEn) "Match colors with wallpaper" else "Sincronizar con fondo de pantalla") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.ColorLens,
                    contentDescription = null
                )
            },
            trailingContent = {
                Switch(
                    checked = dynamicColor,
                    onCheckedChange = { onDynamicColorChange(it) },
                    thumbContent = {
                        if (dynamicColor) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    },
                    colors = switchColors
                )
            },
            colors = listItemColors,
            modifier = modifier.clip(segmentedListItemShape(index, items))
        )
        Spacer(Modifier.height(2.dp))
    }

    Box {
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null
                )
            },
            headlineContent = { Text(if (isEn) "Color scheme preset" else "Paleta de colores") },
            supportingContent = {
                Text(
                    if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (isEn) "Dynamic palette" else "Paleta dinámica"
                    } else {
                        if (isEn) "Custom seed color" else "Color personalizado"
                    }
                )
            },
            colors = listItemColors,
            modifier = modifier.clip(
                RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 4.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
        )

        Box(
            Modifier
                .matchParentSize()
                .clickable(false) {}
        )
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 48.dp),
        modifier = modifier
            .background(
                animateColorAsState(listItemColors.containerColor).value,
                shape = shapes.extraSmall.copy(topStart = CornerSize(0), topEnd = CornerSize(0))
            )
            .padding(bottom = 8.dp)
    ) {
        items(colorSchemes) { color ->
            val isSelected = !dynamicColor && currentColor.value == color.value
            ColorPickerButton(
                color = color,
                isSelected = isSelected,
                enabled = true,
                modifier = Modifier.padding(4.dp)
            ) {
                onDynamicColorChange(false)
                onColorChange(color)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ColorPickerButton(
    color: Color,
    isSelected: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        shapes = IconButtonDefaults.shapes(),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = color,
            disabledContainerColor = color.copy(0.3f)
        ),
        enabled = enabled,
        modifier = modifier.size(48.dp),
        onClick = onClick
    ) {
        AnimatedContent(isSelected, label = "colorSelected") { selected ->
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    tint = Color.Black,
                    contentDescription = null
                )
            }
        }
    }
}
