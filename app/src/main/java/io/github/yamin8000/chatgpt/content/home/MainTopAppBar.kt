/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     MainTopAppBar.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     MainTopAppBar.kt Last modified at 2023/12/1
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

package io.github.yamin8000.chatgpt.content.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Menu
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.yamin8000.chatgpt.R
import io.github.yamin8000.chatgpt.ui.composables.PersianText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsClick: () -> Unit,
    onNavigationClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val title = stringResource(R.string.app_name)
    Surface(
        shadowElevation = 8.dp
    ) {
        TopAppBar(
            scrollBehavior = scrollBehavior,
            title = { PersianText(title) },
            navigationIcon = {
                IconButton(
                    onClick = onNavigationClick,
                    content = {
                        Icon(
                            imageVector = Icons.TwoTone.Menu,
                            contentDescription = null
                        )
                    }
                )
            },
            actions = {
                IconButton(
                    onClick = onSettingsClick,
                    content = {
                        Icon(
                            imageVector = Icons.TwoTone.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                )
                IconButton(
                    onClick = onAboutClick,
                    content = {
                        Icon(
                            imageVector = Icons.TwoTone.Info,
                            contentDescription = stringResource(R.string.about)
                        )
                    }
                )
            }
        )
    }
}