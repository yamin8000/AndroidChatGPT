package io.github.rayangroup.hamyar.content.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.rayangroup.hamyar.R

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