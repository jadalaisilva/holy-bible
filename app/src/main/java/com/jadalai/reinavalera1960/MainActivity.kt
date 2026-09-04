package com.jadalai.reinavalera1960

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.AndroidEntryPoint
import com.jadalai.reinavalera1960.data.local.preferences.AppPreferencesManager
import com.jadalai.reinavalera1960.ui.components.MainAppShell
import com.jadalai.reinavalera1960.ui.onboarding.OnboardingScreen
import com.jadalai.reinavalera1960.ui.reader.ReaderDisplayConfig
import com.jadalai.reinavalera1960.ui.settings.AppLanguage
import com.jadalai.reinavalera1960.ui.settings.BibleTranslation
import com.jadalai.reinavalera1960.ui.theme.BibleAppTheme
import com.jadalai.reinavalera1960.ui.theme.ThemeMode
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: AppPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            var isOnboardingCompleted by remember { mutableStateOf(preferencesManager.isOnboardingCompleted()) }
            var themeMode by remember { mutableStateOf(preferencesManager.getThemeMode()) }
            var dynamicColor by remember { mutableStateOf(preferencesManager.isDynamicColor()) }
            var blackTheme by remember { mutableStateOf(preferencesManager.isBlackTheme()) }
            var seedColor by remember { mutableStateOf(Color(preferencesManager.getSeedColorHex())) }
            var currentLanguage by remember { mutableStateOf(preferencesManager.getAppLanguage()) }
            var displayConfig by remember { mutableStateOf(preferencesManager.getReaderDisplayConfig()) }
            var selectedItemIndex by remember { mutableIntStateOf(0) }

            BibleAppTheme(
                themeMode = themeMode,
                seedColor = seedColor,
                dynamicColor = dynamicColor,
                blackTheme = blackTheme,
                scriptureFont = displayConfig.scriptureFont,
                readingBackgroundTheme = displayConfig.readingBackgroundTheme
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (!isOnboardingCompleted) {
                        OnboardingScreen(
                            currentLanguage = currentLanguage,
                            onLanguageChange = {
                                currentLanguage = it
                                preferencesManager.saveAppLanguage(it)
                            },
                            themeMode = themeMode,
                            onThemeModeChange = {
                                themeMode = it
                                preferencesManager.saveThemeMode(it)
                            },
                            seedColor = seedColor,
                            onSeedColorChange = {
                                seedColor = it
                                dynamicColor = false
                                preferencesManager.saveDynamicColor(false)
                                preferencesManager.saveSeedColorHex(it.value.toLong())
                            },
                            dynamicColor = dynamicColor,
                            onDynamicColorChange = {
                                dynamicColor = it
                                preferencesManager.saveDynamicColor(it)
                            },
                            translation = displayConfig.bibleTranslation,
                            onTranslationChange = {
                                displayConfig = displayConfig.copy(bibleTranslation = it)
                                preferencesManager.saveReaderDisplayConfig(displayConfig)
                            },
                            onFinish = {
                                preferencesManager.setOnboardingCompleted(true)
                                isOnboardingCompleted = true
                            }
                        )
                    } else {
                        MainAppShell(
                            themeMode = themeMode,
                            onThemeModeChange = {
                                themeMode = it
                                preferencesManager.saveThemeMode(it)
                            },
                            seedColor = seedColor,
                            onSeedColorChange = {
                                seedColor = it
                                dynamicColor = false
                                preferencesManager.saveDynamicColor(false)
                                preferencesManager.saveSeedColorHex(it.value.toLong())
                            },
                            dynamicColor = dynamicColor,
                            onDynamicColorChange = {
                                dynamicColor = it
                                preferencesManager.saveDynamicColor(it)
                            },
                            blackTheme = blackTheme,
                            onBlackThemeChange = {
                                blackTheme = it
                                preferencesManager.saveBlackTheme(it)
                            },
                            currentLanguage = currentLanguage,
                            onLanguageChange = {
                                currentLanguage = it
                                preferencesManager.saveAppLanguage(it)
                            },
                            displayConfig = displayConfig,
                            onDisplayConfigChange = {
                                displayConfig = it
                                preferencesManager.saveReaderDisplayConfig(it)
                            },
                            selectedItemIndex = selectedItemIndex,
                            onSelectNavigationIndex = { selectedItemIndex = it }
                        )
                    }
                }
            }
        }
    }
}

