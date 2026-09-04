package com.jadalai.reinavalera1960.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

object BibleShapeDefaults {
    val topListItemShape: RoundedCornerShape
        @Composable get() =
            RoundedCornerShape(
                topStart = shapes.large.topStart,
                topEnd = shapes.large.topEnd,
                bottomStart = shapes.extraSmall.bottomStart,
                bottomEnd = shapes.extraSmall.bottomStart
            )

    val middleListItemShape: RoundedCornerShape
        @Composable get() = RoundedCornerShape(shapes.extraSmall.topStart)

    val bottomListItemShape: RoundedCornerShape
        @Composable get() =
            RoundedCornerShape(
                topStart = shapes.extraSmall.topStart,
                topEnd = shapes.extraSmall.topEnd,
                bottomStart = shapes.large.bottomStart,
                bottomEnd = shapes.large.bottomEnd
            )

    val cardShape: CornerBasedShape
        @Composable get() = shapes.large

    @Composable
    fun segmentedListItemShape(
        index: Int,
        count: Int,
        singleElement: Boolean = count == 1
    ): CornerBasedShape {
        if (singleElement) return shapes.large
        return when (index) {
            0 -> topListItemShape
            count - 1 -> bottomListItemShape
            else -> middleListItemShape
        }
    }

    val PANE_MAX_WIDTH = 600.dp
}
