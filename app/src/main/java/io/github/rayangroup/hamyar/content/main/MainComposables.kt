package io.github.rayangroup.hamyar.content.main

import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.rayangroup.hamyar.ad.TapsellAdContent
import io.github.rayangroup.hamyar.content.ThemeSetting
import io.github.rayangroup.hamyar.content.about.AboutContent
import io.github.rayangroup.hamyar.content.chat.ChatContent
import io.github.rayangroup.hamyar.content.history.HistoryContent
import io.github.rayangroup.hamyar.content.home.HomeContent
import io.github.rayangroup.hamyar.content.images.ImagesContent
import io.github.rayangroup.hamyar.content.settings.SettingsContent
import io.github.rayangroup.hamyar.ui.Nav
import io.github.rayangroup.hamyar.ui.Nav.navigationDestinations
import io.github.rayangroup.hamyar.ui.theme.AppTheme

@Composable
internal fun MainContent(
    currentTheme: ThemeSetting,
    onCreated: (ViewGroup) -> Unit,
    onUpdate: (ViewGroup) -> Unit
) {
    var theme by remember { mutableStateOf(currentTheme) }

    AppTheme(
        isDarkTheme = isDarkTheme(theme, isSystemInDarkTheme()),
        isDynamicColor = theme == ThemeSetting.System
    ) {
        Column {
            val navController = rememberNavController()
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = Nav.Routes.home
            ) {
                composable(Nav.Routes.home) {
                    HomeContent(
                        onNavigateTo = { navigationItem ->
                            navigationDestinations[navigationItem]?.let {
                                navController.navigate(it)
                            }
                        })
                }

                composable(
                    route = "${Nav.Routes.chat}/{${Nav.Args.historyId}}",
                    arguments = listOf(navArgument(Nav.Args.historyId) { type = NavType.LongType })
                ) {
                    val id = it.arguments?.getLong(Nav.Args.historyId) ?: -1L
                    ChatContent(
                        historyId = id,
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

                composable(Nav.Routes.images) {
                    ImagesContent(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            TapsellAdContent(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(4.dp)
                    .fillMaxWidth(),
                onCreated = onCreated,
                onUpdate = onUpdate
            )
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