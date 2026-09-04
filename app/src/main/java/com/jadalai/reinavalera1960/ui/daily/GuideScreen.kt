package com.jadalai.reinavalera1960.ui.daily

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jadalai.reinavalera1960.data.local.entity.BookEntity
import com.jadalai.reinavalera1960.ui.settings.AppLanguage
import com.jadalai.reinavalera1960.ui.settings.BibleTranslation
import com.jadalai.reinavalera1960.ui.theme.GoogleSansFlexTopBarFont
import java.util.Calendar

data class GuideItem(
    val titleEs: String,
    val titleEn: String,
    val icon: ImageVector,
    val bookId: Int,
    val bookName: String,
    val chapterNumber: Int,
    val verseNumber: Int,
    val sampleTextEs: String,
    val sampleTextEn: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideScreen(
    books: List<BookEntity>,
    currentLanguage: AppLanguage,
    translation: BibleTranslation = BibleTranslation.RVR1960,
    onBack: (() -> Unit)? = null,
    onNavigateToChapter: (BookEntity, Int, Long?) -> Unit
) {
    val effectiveLang = com.jadalai.reinavalera1960.ui.i18n.resolveLanguage(currentLanguage)
    val isEnUI = effectiveLang == AppLanguage.ENGLISH
    val isKjv = translation == BibleTranslation.KJV
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    data class DailyVerse(
        val bookId: Int,
        val chapter: Int,
        val verseNum: Int,
        val textEs: String,
        val textEn: String
    )

    val dailyVerses = remember {
        listOf(
            DailyVerse(45, 12, 12, "Gozosos en la esperanza; sufridos en la tribulación; constantes en la oración.", "Rejoicing in hope; patient in tribulation; continuing instant in prayer."),
            DailyVerse(19, 46, 10, "Estad quietos, y conoced que yo soy Dios; Seré exaltado entre las naciones; enaltecido seré en la tierra.", "Be still, and know that I am God: I will be exalted among the heathen, I will be exalted in the earth."),
            DailyVerse(62, 1, 9, "Si confesamos nuestros pecados, él es fiel y justo para perdonar nuestros pecados, y limpiarnos de toda maldad.", "If we confess our sins, he is faithful and just to forgive us our sins, and to cleanse us from all unrighteousness."),
            DailyVerse(50, 4, 13, "Todo lo puedo en Cristo que me fortalece.", "I can do all things through Christ which strengtheneth me."),
            DailyVerse(19, 51, 1, "Ten piedad de mí, oh Dios, conforme a tu misericordia; Conforme a la multitud de tus piedades borra mis rebeliones.", "Have mercy upon me, O God, according to thy lovingkindness: according unto the multitude of thy tender mercies blot out my transgressions."),
            DailyVerse(23, 40, 29, "El da esfuerzo al cansado, y multiplica las fuerzas al que no tiene ningunas.", "He giveth power to the faint; and to them that have no might he increaseth strength."),
            DailyVerse(19, 27, 13, "Hubiera yo desmayado, si no creyese que veré la bondad de Jehová En la tierra de los vivientes.", "I had fainted, unless I had believed to see the goodness of the LORD in the land of the living."),
            DailyVerse(19, 139, 14, "Te alabaré; porque formidables, maravillosas son tus obras; Estoy maravillado, Y mi alma lo sabe muy bien.", "I will praise thee; for I am fearfully and wonderfully made: marvellous are thy words; and that my soul knoweth right well."),
            DailyVerse(52, 5, 11, "Por lo cual, animaos unos a otros, y edificaos unos a otros, así como lo hacéis.", "Wherefore comfort yourselves together, and edify one another, even as also ye do."),
            DailyVerse(19, 63, 1, "Dios, Dios mío eres tú; De madrugada te buscaré; Mi alma tiene sed de ti, mi carne te anhela, En tierra seca y árida donde no hay aguas.", "O God, thou art my God; early will I seek thee: my soul thirsteth for thee, my flesh longeth for thee in a dry and thirsty land, where no water is."),
            DailyVerse(49, 6, 10, "Por lo demás, hermanos míos, fortaleceos en el Señor, y en el poder de su fuerza.", "Finally, my brethren, be strong in the Lord, and in the power of his might."),
            DailyVerse(19, 16, 8, "A Jehová he puesto siempre delante de mí; Porque está a mi diestra, no seré conmovido.", "I have set the LORD always before me: because he is at my right hand, I shall not be moved."),
            DailyVerse(55, 3, 16, "Toda la Escritura es inspirada por Dios, y útil para enseñar, para redargüir, para corregir, para instruir en justicia.", "All scripture is given by inspiration of God, and is profitable for doctrine, for reproof, for correction, for instruction in righteousness:"),
            DailyVerse(19, 34, 18, "Cercano está Jehová a los quebrantados de corazón; Y salva a los contritos de espíritu.", "The LORD is nigh unto them that are of a broken heart; and saveth such as be of a contrite spirit.")
        )
    }

    val dayOfYear = remember { Calendar.getInstance().get(Calendar.DAY_OF_YEAR) }
    val todayVerse = remember(dayOfYear) {
        dailyVerses[dayOfYear % dailyVerses.size]
    }
    val targetDailyBook = books.find { it.id == todayVerse.bookId }

    val guideItems = remember {
        listOf(
            GuideItem(
                titleEs = "Amor",
                titleEn = "Love",
                icon = Icons.Default.Favorite,
                bookId = 43,
                bookName = "Juan",
                chapterNumber = 3,
                verseNumber = 16,
                sampleTextEs = "Porque de tal manera amó Dios al mundo, que ha dado a su Hijo unigénito...",
                sampleTextEn = "For God so loved the world, that he gave his only begotten Son..."
            ),
            GuideItem(
                titleEs = "Fortaleza",
                titleEn = "Strength",
                icon = Icons.Default.SelfImprovement,
                bookId = 6,
                bookName = "Josué",
                chapterNumber = 1,
                verseNumber = 9,
                sampleTextEs = "Mira que te mando que te esfuerces y seas valiente; no temas ni desmayes...",
                sampleTextEn = "Have not I commanded thee? Be strong and of a good courage; be not afraid..."
            ),
            GuideItem(
                titleEs = "Perdón",
                titleEn = "Forgiveness",
                icon = Icons.Default.VolunteerActivism,
                bookId = 49,
                bookName = "Efesios",
                chapterNumber = 4,
                verseNumber = 32,
                sampleTextEs = "Antes sed benignos unos con otros, misericordiosos, perdonándoos unos a otros...",
                sampleTextEn = "And be ye kind one to another, tenderhearted, forgiving one another..."
            ),
            GuideItem(
                titleEs = "Salvación",
                titleEn = "Salvation",
                icon = Icons.Default.AutoAwesome,
                bookId = 45,
                bookName = "Romanos",
                chapterNumber = 10,
                verseNumber = 9,
                sampleTextEs = "Que si confesares con tu boca que Jesús es el Señor, y creyeres en tu corazón...",
                sampleTextEn = "That if thou shalt confess with thy mouth the Lord Jesus, and shalt believe in thine heart..."
            ),
            GuideItem(
                titleEs = "Paz",
                titleEn = "Peace",
                icon = Icons.Default.Spa,
                bookId = 43,
                bookName = "Juan",
                chapterNumber = 14,
                verseNumber = 27,
                sampleTextEs = "La paz os dejo, mi paz os doy; yo no os la doy como el mundo la da. No se turbe vuestro corazón.",
                sampleTextEn = "Peace I leave with you, my peace I give unto you: not as the world giveth, give I unto you."
            ),
            GuideItem(
                titleEs = "Fe",
                titleEn = "Faith",
                icon = Icons.Default.Bookmark,
                bookId = 58,
                bookName = "Hebreos",
                chapterNumber = 11,
                verseNumber = 1,
                sampleTextEs = "Es, pues, la fe la certeza de lo que se espera, la convicción de lo que no se ve.",
                sampleTextEn = "Now faith is the substance of things hoped for, the evidence of things not seen."
            ),
            GuideItem(
                titleEs = "Consejo",
                titleEn = "Counsel",
                icon = Icons.Default.Category,
                bookId = 59,
                bookName = "Santiago",
                chapterNumber = 1,
                verseNumber = 5,
                sampleTextEs = "Y si alguno de vosotros tiene falta de sabiduría, pídala a Dios, el cual da a todos abundantemente...",
                sampleTextEn = "If any of you lack wisdom, let him ask of God, that giveth to all men liberally..."
            )
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = if (isEnUI) "Guide" else "Guía",
                        style = com.jadalai.reinavalera1960.ui.theme.LocalAppFonts.current.topBarTitle,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Daily Verse Highlight Card
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (targetDailyBook != null) {
                                val dailyVerseId = targetDailyBook.id.toLong() * 1000000L + todayVerse.chapter * 1000L + todayVerse.verseNum
                                onNavigateToChapter(targetDailyBook, todayVerse.chapter, dailyVerseId)
                            }
                        }
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = if (isEnUI) "Verse of the day" else "Versículo del día",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                val dailyBookName = targetDailyBook?.let {
                                    com.jadalai.reinavalera1960.ui.i18n.AppStrings.getLocalizedBookName(it.id, it.name, isKjv)
                                } ?: (if (isKjv) "Psalms" else "Salmos")
                                Text(
                                    text = "$dailyBookName ${todayVerse.chapter}:${todayVerse.verseNum}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "\"${if (isKjv) todayVerse.textEn else todayVerse.textEs}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isEnUI) "Tap to open chapter →" else "Toca para abrir capítulo →",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Section Header for Guide
            item {
                Text(
                    text = if (isEnUI) "Guide by topics" else "Guía por temas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                )
            }

            // Thematic Guide list
            items(guideItems) { guide ->
                val guideBook = books.find { it.id == guide.bookId }
                val localizedTopicBookName = guideBook?.let {
                    com.jadalai.reinavalera1960.ui.i18n.AppStrings.getLocalizedBookName(it.id, it.name, isKjv)
                } ?: com.jadalai.reinavalera1960.ui.i18n.AppStrings.getLocalizedBookName(guide.bookId, guide.bookName, isKjv)

                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (guideBook != null) {
                                val topicVerseId = guideBook.id.toLong() * 1000000L + guide.chapterNumber * 1000L + guide.verseNumber
                                onNavigateToChapter(guideBook, guide.chapterNumber, topicVerseId)
                            }
                        }
                ) {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = if (isEnUI) guide.titleEn else guide.titleEs,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        supportingContent = {
                            Column {
                                Text(
                                    text = "$localizedTopicBookName ${guide.chapterNumber}:${guide.verseNumber}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = if (isKjv) guide.sampleTextEn else guide.sampleTextEs,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2
                                )
                            }
                        },
                        leadingContent = {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = guide.icon,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}
