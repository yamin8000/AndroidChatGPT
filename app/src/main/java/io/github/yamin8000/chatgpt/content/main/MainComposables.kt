/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     MainComposables.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     MainComposables.kt Last modified at 2023/12/1
 *     This file is part of AndroidChatGPT/AndroidChatGPT.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     AndroidChatGPT/AndroidChatGPT.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     AndroidChatGPT/AndroidChatGPT.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with AndroidChatGPT.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.chatgpt.content.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.yamin8000.chatgpt.content.ThemeSetting
import io.github.yamin8000.chatgpt.content.about.AboutContent
import io.github.yamin8000.chatgpt.content.chat.ChatContent
import io.github.yamin8000.chatgpt.content.history.HistoryContent
import io.github.yamin8000.chatgpt.content.home.HomeContent
import io.github.yamin8000.chatgpt.content.images.ImagesContent
import io.github.yamin8000.chatgpt.content.settings.SettingsContent
import io.github.yamin8000.chatgpt.ui.Nav
import io.github.yamin8000.chatgpt.ui.Nav.navigationDestinations
import io.github.yamin8000.chatgpt.ui.theme.AppTheme

@Composable
internal fun MainContent(
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
    }
}

private fun isDarkTheme(
    themeSetting: ThemeSetting,
    isSystemInDarkTheme: Boolean
): Boolean {
    if (themeSetting == ThemeSetting.Light) return false
    if (themeSetting == ThemeSetting.System) return isSystemInDarkTheme
    return themeSetting == ThemeSetting.Dark
}