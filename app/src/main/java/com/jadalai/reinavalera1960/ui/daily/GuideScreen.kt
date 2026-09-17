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
import androidx.compose.material.icons.filled.RecordVoiceOver
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

import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class TopicVerseRef(
    val bookId: Int,
    val bookNameEs: String,
    val chapter: Int,
    val startVerse: Int,
    val endVerse: Int = startVerse
) {
    val displayRefEs: String
        get() = if (startVerse == endVerse) "$bookNameEs $chapter:$startVerse" else "$bookNameEs $chapter:$startVerse-$endVerse"
}

data class GuideItem(
    val id: String,
    val titleEs: String,
    val titleEn: String,
    val icon: ImageVector,
    val verses: List<TopicVerseRef>
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

    var selectedTopic by remember { mutableStateOf<GuideItem?>(null) }

    val loveVerses = remember {
        listOf(
            TopicVerseRef(46, "1 Corintios", 13, 4, 5),
            TopicVerseRef(51, "Colosenses", 3, 14, 14),
            TopicVerseRef(20, "Proverbios", 3, 3, 4),
            TopicVerseRef(62, "1 Juan", 4, 19, 19),
            TopicVerseRef(49, "Efesios", 4, 2, 2),
            TopicVerseRef(48, "Gálatas", 5, 14, 14),
            TopicVerseRef(43, "Juan", 14, 15, 15),
            TopicVerseRef(40, "Mateo", 22, 37, 40),
            TopicVerseRef(60, "1 Pedro", 4, 8, 8),
            TopicVerseRef(45, "Romanos", 12, 9, 9),
            TopicVerseRef(53, "2 Tesalonicenses", 3, 5, 5),
            TopicVerseRef(45, "Romanos", 12, 10, 10),
            TopicVerseRef(62, "1 Juan", 4, 12, 12),
            TopicVerseRef(43, "Juan", 14, 21, 24),
            TopicVerseRef(62, "1 Juan", 4, 21, 21),
            TopicVerseRef(43, "Juan", 15, 13, 13),
            TopicVerseRef(62, "1 Juan", 4, 18, 18),
            TopicVerseRef(46, "1 Corintios", 13, 13, 13)
        )
    }

    val strengthVerses = remember {
        listOf(
            TopicVerseRef(2, "Éxodo", 15, 2, 2),
            TopicVerseRef(5, "Deuteronomio", 31, 6, 6),
            TopicVerseRef(19, "Salmos", 18, 2, 2),
            TopicVerseRef(6, "Josué", 1, 9, 9),
            TopicVerseRef(19, "Salmos", 37, 39, 39),
            TopicVerseRef(23, "Isaías", 12, 2, 2),
            TopicVerseRef(19, "Salmos", 46, 1, 1),
            TopicVerseRef(23, "Isaías", 40, 29, 29),
            TopicVerseRef(19, "Salmos", 71, 3, 3),
            TopicVerseRef(23, "Isaías", 41, 10, 10),
            TopicVerseRef(34, "Nahúm", 1, 7, 7),
            TopicVerseRef(35, "Habacuc", 3, 19, 19),
            TopicVerseRef(50, "Filipenses", 4, 13, 13),
            TopicVerseRef(55, "2 Timoteo", 2, 1, 1),
            TopicVerseRef(23, "Isaías", 33, 2, 2),
            TopicVerseRef(6, "Josué", 1, 7, 7),
            TopicVerseRef(19, "Salmos", 119, 105, 105),
            TopicVerseRef(55, "2 Timoteo", 1, 7, 7),
            TopicVerseRef(19, "Salmos", 55, 22, 22),
            TopicVerseRef(50, "Filipenses", 4, 6, 7),
            TopicVerseRef(46, "1 Corintios", 16, 13, 13)
        )
    }
    val forgivenessVerses = remember {
        listOf(
            TopicVerseRef(40, "Mateo", 6, 15, 15),
            TopicVerseRef(43, "Juan", 8, 11, 11),
            TopicVerseRef(42, "Lucas", 23, 34, 34),
            TopicVerseRef(40, "Mateo", 5, 7, 7),
            TopicVerseRef(42, "Lucas", 6, 37, 37),
            TopicVerseRef(19, "Salmos", 86, 5, 5),
            TopicVerseRef(41, "Marcos", 11, 25, 25),
            TopicVerseRef(58, "Hebreos", 8, 12, 12),
            TopicVerseRef(40, "Mateo", 18, 21, 22),
            TopicVerseRef(19, "Salmos", 18, 25, 25),
            TopicVerseRef(49, "Efesios", 4, 32, 32),
            TopicVerseRef(51, "Colosenses", 3, 13, 13),
            TopicVerseRef(58, "Hebreos", 12, 15, 15),
            TopicVerseRef(33, "Miqueas", 7, 18, 18),
            TopicVerseRef(14, "2 Crónicas", 30, 9, 9),
            TopicVerseRef(62, "1 Juan", 2, 2, 2),
            TopicVerseRef(19, "Salmos", 32, 5, 5),
            TopicVerseRef(23, "Isaías", 55, 7, 7),
            TopicVerseRef(59, "Santiago", 5, 14, 15),
            TopicVerseRef(32, "Jonás", 3, 10, 10),
            TopicVerseRef(44, "Hechos", 10, 43, 43)
        )
    }

    val salvationVerses = remember {
        listOf(
            TopicVerseRef(47, "2 Corintios", 5, 17, 17),
            TopicVerseRef(43, "Juan", 10, 28, 28),
            TopicVerseRef(43, "Juan", 3, 36, 36),
            TopicVerseRef(45, "Romanos", 3, 23, 23),
            TopicVerseRef(43, "Juan", 1, 12, 12),
            TopicVerseRef(55, "2 Timoteo", 1, 9, 9),
            TopicVerseRef(19, "Salmos", 62, 1, 1),
            TopicVerseRef(44, "Hechos", 2, 21, 21),
            TopicVerseRef(44, "Hechos", 4, 12, 12),
            TopicVerseRef(43, "Juan", 3, 16, 16),
            TopicVerseRef(42, "Lucas", 18, 27, 27),
            TopicVerseRef(58, "Hebreos", 9, 22, 22),
            TopicVerseRef(66, "Apocalipsis", 3, 20, 20),
            TopicVerseRef(62, "1 Juan", 1, 9, 9),
            TopicVerseRef(45, "Romanos", 10, 9, 10),
            TopicVerseRef(56, "Tito", 3, 5, 5),
            TopicVerseRef(44, "Hechos", 16, 31, 31),
            TopicVerseRef(19, "Salmos", 18, 35, 36),
            TopicVerseRef(46, "1 Corintios", 15, 1, 2),
            TopicVerseRef(40, "Mateo", 16, 25, 25)
        )
    }

    val peaceVerses = remember {
        listOf(
            TopicVerseRef(58, "Hebreos", 12, 11, 11),
            TopicVerseRef(45, "Romanos", 12, 18, 18),
            TopicVerseRef(23, "Isaías", 54, 10, 10),
            TopicVerseRef(20, "Proverbios", 12, 20, 20),
            TopicVerseRef(45, "Romanos", 1, 7, 7),
            TopicVerseRef(40, "Mateo", 10, 34, 34),
            TopicVerseRef(55, "2 Timoteo", 2, 22, 22),
            TopicVerseRef(49, "Efesios", 6, 14, 16),
            TopicVerseRef(44, "Hechos", 9, 31, 31),
            TopicVerseRef(45, "Romanos", 16, 20, 20),
            TopicVerseRef(58, "Hebreos", 13, 20, 21),
            TopicVerseRef(43, "Juan", 20, 21, 21),
            TopicVerseRef(42, "Lucas", 2, 14, 14),
            TopicVerseRef(20, "Proverbios", 3, 1, 2)
        )
    }

    val happinessVerses = remember {
        listOf(
            TopicVerseRef(19, "Salmos", 16, 11, 11),
            TopicVerseRef(19, "Salmos", 30, 5, 5),
            TopicVerseRef(19, "Salmos", 5, 11, 11),
            TopicVerseRef(43, "Juan", 15, 11, 11),
            TopicVerseRef(19, "Salmos", 19, 8, 8),
            TopicVerseRef(23, "Isaías", 35, 10, 10),
            TopicVerseRef(20, "Proverbios", 17, 22, 22),
            TopicVerseRef(20, "Proverbios", 15, 15, 15),
            TopicVerseRef(42, "Lucas", 15, 10, 10),
            TopicVerseRef(19, "Salmos", 1, 1, 3),
            TopicVerseRef(19, "Salmos", 4, 7, 7),
            TopicVerseRef(19, "Salmos", 33, 21, 21),
            TopicVerseRef(19, "Salmos", 34, 5, 5),
            TopicVerseRef(45, "Romanos", 14, 17, 17)
        )
    }

    val faithVerses = remember {
        listOf(
            TopicVerseRef(43, "Juan", 3, 16, 16),
            TopicVerseRef(59, "Santiago", 2, 17, 17),
            TopicVerseRef(40, "Mateo", 17, 20, 20),
            TopicVerseRef(43, "Juan", 20, 29, 29),
            TopicVerseRef(42, "Lucas", 8, 50, 50),
            TopicVerseRef(35, "Habacuc", 2, 4, 4),
            TopicVerseRef(45, "Romanos", 3, 21, 22),
            TopicVerseRef(58, "Hebreos", 3, 14, 14),
            TopicVerseRef(46, "1 Corintios", 15, 1, 2),
            TopicVerseRef(48, "Gálatas", 2, 15, 16),
            TopicVerseRef(58, "Hebreos", 11, 1, 1)
        )
    }

    val guideItems = remember {
        listOf(
            GuideItem(
                id = "love",
                titleEs = "Amor",
                titleEn = "Love",
                icon = Icons.Default.Favorite,
                verses = loveVerses
            ),
            GuideItem(
                id = "happiness",
                titleEs = "Felicidad",
                titleEn = "Happiness",
                icon = Icons.Default.WbSunny,
                verses = happinessVerses
            ),
            GuideItem(
                id = "strength",
                titleEs = "Fortaleza",
                titleEn = "Strength",
                icon = Icons.Default.SelfImprovement,
                verses = strengthVerses
            ),
            GuideItem(
                id = "forgiveness",
                titleEs = "Perdón",
                titleEn = "Forgiveness",
                icon = Icons.Default.VolunteerActivism,
                verses = forgivenessVerses
            ),
            GuideItem(
                id = "salvation",
                titleEs = "Salvación",
                titleEn = "Salvation",
                icon = Icons.Default.AutoAwesome,
                verses = salvationVerses
            ),
            GuideItem(
                id = "peace",
                titleEs = "Paz",
                titleEn = "Peace",
                icon = Icons.Default.Spa,
                verses = peaceVerses
            ),
            GuideItem(
                id = "faith",
                titleEs = "Fe",
                titleEn = "Faith",
                icon = Icons.Default.Bookmark,
                verses = faithVerses
            )
        )
    }

    val activeTopic = selectedTopic
    if (activeTopic != null) {
        TopicDetailSubpage(
            topic = activeTopic,
            books = books,
            isEnUI = isEnUI,
            isKjv = isKjv,
            onBack = { selectedTopic = null },
            onNavigateToChapter = onNavigateToChapter
        )
        return
    }

    data class DailyVerse(
        val bookId: Int,
        val chapter: Int,
        val verseNum: Int,
        val textEs: String,
        val textEn: String
    )

    val dailyVerses = remember(guideItems) {
        val initialList = listOf(
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

        val topicDailyVerses = guideItems.flatMap { item ->
            item.verses.map { v ->
                DailyVerse(
                    bookId = v.bookId,
                    chapter = v.chapter,
                    verseNum = v.startVerse,
                    textEs = v.displayRefEs,
                    textEn = v.displayRefEs
                )
            }
        }
        (initialList + topicDailyVerses).distinctBy { "${it.bookId}_${it.chapter}_${it.verseNum}" }
    }

    val dayOfYear = remember { Calendar.getInstance().get(Calendar.DAY_OF_YEAR) }
    val todayVerse = remember(dayOfYear) {
        dailyVerses[dayOfYear % dailyVerses.size]
    }
    val targetDailyBook = books.find { it.id == todayVerse.bookId }

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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isEnUI) "Tap to open chapter →" else "Toca para abrir capítulo →",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            val context = androidx.compose.ui.platform.LocalContext.current
                            IconButton(
                                onClick = {
                                    val verseText = if (isKjv) todayVerse.textEn else todayVerse.textEs
                                    com.jadalai.reinavalera1960.service.TextToSpeechManager.getInstance(context).speak(verseText, isKjv)
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = "Leer versículo en voz alta",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
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
                val firstVerse = guide.verses.firstOrNull()
                val countVerses = guide.verses.size
                val guideBook = books.find { it.id == firstVerse?.bookId }
                val localizedTopicBookName = firstVerse?.let { vRef ->
                    guideBook?.let {
                        com.jadalai.reinavalera1960.ui.i18n.AppStrings.getLocalizedBookName(it.id, it.name, isKjv)
                    } ?: com.jadalai.reinavalera1960.ui.i18n.AppStrings.getLocalizedBookName(vRef.bookId, vRef.bookNameEs, isKjv)
                } ?: ""

                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedTopic = guide
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
                            if (firstVerse != null) {
                                Column {
                                    Text(
                                        text = if (countVerses > 1) {
                                            if (isEnUI) "$countVerses verses" else "$countVerses versículos"
                                        } else {
                                            "$localizedTopicBookName ${firstVerse.chapter}:${if (firstVerse.startVerse == firstVerse.endVerse) "${firstVerse.startVerse}" else "${firstVerse.startVerse}-${firstVerse.endVerse}"}"
                                        },
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
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
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicDetailSubpage(
    topic: GuideItem,
    books: List<BookEntity>,
    isEnUI: Boolean,
    isKjv: Boolean,
    onBack: () -> Unit,
    onNavigateToChapter: (BookEntity, Int, Long?) -> Unit
) {
    androidx.activity.compose.BackHandler(onBack = onBack)

    val topicTitle = if (isEnUI) topic.titleEn else topic.titleEs
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = {
            LargeTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = topic.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = topicTitle,
                            style = com.jadalai.reinavalera1960.ui.theme.LocalAppFonts.current.topBarTitle,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isEnUI) "Verses about $topicTitle" else "Versículos de $topicTitle",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(topic.verses) { vRef ->
                val targetBook = books.find { it.id == vRef.bookId }
                val bookName = targetBook?.let {
                    com.jadalai.reinavalera1960.ui.i18n.AppStrings.getLocalizedBookName(it.id, it.name, isKjv)
                } ?: com.jadalai.reinavalera1960.ui.i18n.AppStrings.getLocalizedBookName(vRef.bookId, vRef.bookNameEs, isKjv)

                val verseRefText = if (vRef.startVerse == vRef.endVerse) {
                    "$bookName ${vRef.chapter}:${vRef.startVerse}"
                } else {
                    "$bookName ${vRef.chapter}:${vRef.startVerse}-${vRef.endVerse}"
                }

                ElevatedCard(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (targetBook != null) {
                                val verseId = targetBook.id.toLong() * 1000000L + vRef.chapter * 1000L + vRef.startVerse
                                onNavigateToChapter(targetBook, vRef.chapter, verseId)
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Text(
                                text = verseRefText,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}



