package io.github.aryantech.androidchatgpt.content

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import io.github.aryantech.androidchatgpt.content.about.AboutContent
import io.github.aryantech.androidchatgpt.content.chat.ChatContent
import io.github.aryantech.androidchatgpt.content.history.HistoryContent
import io.github.aryantech.androidchatgpt.content.home.HomeContent
import io.github.aryantech.androidchatgpt.content.settings.SettingsContent
import io.github.aryantech.androidchatgpt.db.AppDatabase
import io.github.aryantech.androidchatgpt.ui.Nav
import io.github.aryantech.androidchatgpt.ui.theme.AppTheme
import io.github.aryantech.androidchatgpt.util.Constants
import io.github.aryantech.androidchatgpt.util.Constants.db
import io.github.aryantech.androidchatgpt.util.Constants.isDbInitialized
import io.github.aryantech.androidchatgpt.util.DataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.Main)

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scope.launch {
            db = createDb()
            val theme = getCurrentTheme()
            setContent { Scaffold { MainContent(theme) } }
        }

        var locales = AppCompatDelegate.getApplicationLocales()
        if (locales.isEmpty)
            locales = LocaleListCompat.forLanguageTags(Constants.DEFAULT_LANGUAGE_TAGS)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    private suspend fun getCurrentTheme() = ThemeSetting.valueOf(
        DataStoreHelper(settingsDataStore).getString(Constants.THEME) ?: ThemeSetting.System.name
    )

    private fun createDb(): AppDatabase {
        return if (!isDbInitialized())
            Room.databaseBuilder(
                this,
                AppDatabase::class.java,
                "db"
            ).build()
        else db
    }
}

@Composable
fun MainContent(
    currentTheme: ThemeSetting
) {
    var theme by remember { mutableStateOf(currentTheme) }

    AppTheme(
        isDarkTheme = isDarkTheme(theme, isSystemInDarkTheme()),
        isDynamicColor = theme == ThemeSetting.System
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Nav.Routes.home
        ) {
            composable(Nav.Routes.home) {
                HomeContent(
                    onNavigateToSettings = { navController.navigate(Nav.Routes.settings) },
                    onNavigateToNewChat = { navController.navigate("${Nav.Routes.chat}/-1") },
                    onNavigateToHistory = { navController.navigate(Nav.Routes.history) },
                    onNavigateToAbout = { navController.navigate(Nav.Routes.about) }
                )
            }

            composable("${Nav.Routes.chat}/{${Nav.Args.historyId}}") {
                ChatContent(
                    historyId = it.arguments?.getString(Nav.Args.historyId),
                    onBackClick = { navController.popBackStack() },
                    onSettingsClick = { navController.navigate(Nav.Routes.settings) }
                )
            }

            composable(Nav.Routes.settings) {
                SettingsContent(
                    onThemeChanged = { newTheme -> theme = newTheme },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Nav.Routes.about) {
                AboutContent(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Nav.Routes.history) {
                HistoryContent(
                    onBackClick = { navController.popBackStack() },
                    onItemClick = { historyId -> navController.navigate("${Nav.Routes.chat}/$historyId") }
                )
            }
        }
    }
}

private fun isDarkTheme(
    themeSetting: ThemeSetting,
    isSystemInDarkTheme: Boolean
): Boolean {
    if (themeSetting == ThemeSetting.Light) return false
    if (themeSetting == ThemeSetting.System) return isSystemInDarkTheme
    if (themeSetting == ThemeSetting.Dark) return true
    return false
}