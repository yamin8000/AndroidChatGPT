package io.github.aryantech.androidchatgpt.content

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.aryantech.androidchatgpt.content.chat.ChatContent
import io.github.aryantech.androidchatgpt.content.home.HomeContent
import io.github.aryantech.androidchatgpt.content.settings.SettingsContent
import io.github.aryantech.androidchatgpt.ui.Nav
import io.github.aryantech.androidchatgpt.ui.theme.AppTheme
import io.github.aryantech.androidchatgpt.util.Constants
import io.github.aryantech.androidchatgpt.util.DataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {

    private val scope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scope.launch {
            val theme = getCurrentTheme()
            setContent { Scaffold { MainContent(theme) } }
        }
    }

    private suspend fun getCurrentTheme() = ThemeSetting.valueOf(
        DataStoreHelper(settingsDataStore).getString(Constants.THEME) ?: ThemeSetting.System.name
    )
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
                    onSettingsClick = { navController.navigate(Nav.Routes.settings) },
                    onNewChat = { navController.navigate(Nav.Routes.chat) }
                )
            }

            composable(Nav.Routes.chat) {
                ChatContent(
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

            }

            composable(Nav.Routes.history) {

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