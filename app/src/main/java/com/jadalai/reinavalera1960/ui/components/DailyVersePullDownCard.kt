package com.jadalai.reinavalera1960.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jadalai.reinavalera1960.data.local.entity.BookEntity
import java.util.Calendar

@Composable
fun DailyVersePullDownCard(
    books: List<BookEntity>,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onNavigateToChapter: (BookEntity, Int, Long?) -> Unit
) {
    data class PullDownDailyVerse(
        val bookId: Int,
        val chapter: Int,
        val verseNum: Int,
        val text: String
    )

    val dailyVerses = remember {
        listOf(
            PullDownDailyVerse(19, 23, 1, "El Señor es mi pastor; nada me faltará. En lugares de delicados pastos me hará descansar."),
            PullDownDailyVerse(43, 3, 16, "Porque de tal manera amó Dios al mundo, que ha dado a su Hijo unigénito, para que todo aquel que en él cree no se pierda, mas tenga vida eterna."),
            PullDownDailyVerse(50, 4, 13, "Todo lo puedo en Cristo que me fortalece."),
            PullDownDailyVerse(40, 11, 28, "Venid a mí todos los que estáis trabajados y cargados, y yo os haré descansar."),
            PullDownDailyVerse(20, 3, 5, "Fíate del Señor de todo tu corazón, y no te apoyes en tu propia prudencia."),
            PullDownDailyVerse(45, 8, 28, "Y sabemos que a los que aman a Dios, todas las cosas les ayudan a bien."),
            PullDownDailyVerse(58, 11, 1, "Es, pues, la fe la certeza de lo que se espera, la convicción de lo que no se ve.")
        )
    }

    val dayOfYear = remember { Calendar.getInstance().get(Calendar.DAY_OF_YEAR) }
    val todayDaily = remember(dayOfYear) {
        dailyVerses[dayOfYear % dailyVerses.size]
    }
    val targetBook = books.find { it.id == todayDaily.bookId }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) + expandVertically(),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) + shrinkVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (targetBook != null) {
                            val verseId = targetBook.id.toLong() * 1000000L + todayDaily.chapter * 1000L + todayDaily.verseNum
                            onNavigateToChapter(targetBook, todayDaily.chapter, verseId)
                        }
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Versículo del Día",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"${todayDaily.text}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${targetBook?.name ?: "Salmos"} ${todayDaily.chapter}:${todayDaily.verseNum}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
