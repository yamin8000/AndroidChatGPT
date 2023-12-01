/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     NavigationItem.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     NavigationItem.kt Last modified at 2023/12/1
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
import androidx.compose.material.icons.twotone.*
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.yamin8000.chatgpt.R

enum class NavigationItem(
    val labelId: Int,
    val icon: ImageVector
) {
    Home(R.string.home, Icons.TwoTone.Home),
    NewChat(R.string.new_chat, Icons.TwoTone.Chat),
    History(R.string.chat_history, Icons.TwoTone.History),
    Images(R.string.image_generation, Icons.TwoTone.ImageSearch),
    Settings(R.string.settings, Icons.TwoTone.Settings),
    About(R.string.about, Icons.TwoTone.Info)
}