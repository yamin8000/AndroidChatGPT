package io.github.aryantech.androidchatgpt.content.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.aryantech.androidchatgpt.R

enum class NavigationItem(
    val labelId: Int,
    val icon: ImageVector
) {
    Home(R.string.home, Icons.TwoTone.Home),
    NewChat(R.string.new_chat, Icons.TwoTone.Chat),
    History(R.string.history, Icons.TwoTone.History),
    Settings(R.string.settings, Icons.TwoTone.Settings),
    About(R.string.about, Icons.TwoTone.Info)
}