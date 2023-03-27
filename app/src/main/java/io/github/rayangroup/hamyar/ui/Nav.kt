package io.github.rayangroup.hamyar.ui

import io.github.rayangroup.hamyar.content.home.NavigationItem

object Nav {

    object Routes {
        const val home = "home"
        const val chat = "chat"
        const val history = "history"
        const val settings = "settings"
        const val about = "about"
        const val images = "images"
    }

    object Args {
        const val historyId = "historyId"
    }

    val navigationDestinations = mapOf(
        NavigationItem.NewChat to "${Routes.chat}/-1",
        NavigationItem.History to Routes.history,
        NavigationItem.Settings to Routes.settings,
        NavigationItem.About to Routes.about,
        NavigationItem.Images to Routes.images,
    )
}