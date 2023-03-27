package io.github.RayanGroup.hamyar.content

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import io.github.RayanGroup.hamyar.ad.AdConstants
import io.github.RayanGroup.hamyar.ad.TapsellAdContent
import io.github.RayanGroup.hamyar.content.about.AboutContent
import io.github.RayanGroup.hamyar.content.chat.ChatContent
import io.github.RayanGroup.hamyar.content.history.HistoryContent
import io.github.RayanGroup.hamyar.content.home.HomeContent
import io.github.RayanGroup.hamyar.content.home.NavigationItem
import io.github.RayanGroup.hamyar.content.images.ImagesContent
import io.github.RayanGroup.hamyar.content.settings.SettingsContent
import io.github.RayanGroup.hamyar.db.AppDatabase
import io.github.RayanGroup.hamyar.ui.Nav
import io.github.RayanGroup.hamyar.ui.theme.AppTheme
import io.github.RayanGroup.hamyar.util.Constants
import io.github.RayanGroup.hamyar.util.Constants.db
import io.github.RayanGroup.hamyar.util.Constants.isDbInitialized
import io.github.RayanGroup.hamyar.util.DataStoreHelper
import io.github.RayanGroup.hamyar.util.log
import ir.tapsell.plus.*
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
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

        db = createDb()
        scope.launch {
            val theme = getCurrentTheme()
            setContent {
                var adView by remember { mutableStateOf<ViewGroup?>(null) }
                var adId by remember { mutableStateOf("") }

                TapsellPlus.showStandardBannerAd(
                    this@MainActivity,
                    adId,
                    adView,
                    object : AdShowListener() {
                        override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                            super.onOpened(tapsellPlusAdModel)
                            tapsellPlusAdModel.responseId.log()
                        }

                        override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                            super.onError(tapsellPlusErrorModel)
                            tapsellPlusErrorModel.errorMessage.log()
                        }
                    })

                LaunchedEffect(Unit) {
                    TapsellPlus.requestStandardBannerAd(
                        this@MainActivity,
                        AdConstants.STANDARD_BANNER_ZONE_ID,
                        TapsellPlusBannerType.BANNER_320x50,
                        object : AdRequestCallback() {
                            override fun response(ad: TapsellPlusAdModel?) {
                                super.response(ad)
                                adId = ad?.responseId ?: ""
                                adId.log()
                            }

                            override fun error(error: String?) {
                                super.error(error)
                                error?.log()
                            }
                        }
                    )
                }

                Scaffold {
                    Column {
                        MainContent(
                            currentTheme = theme,
                            modifier = Modifier.weight(1f)
                        )
                        TapsellAdContent(
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth(),
                            onCreated = { adView = it },
                            onUpdate = { adView = it }
                        )
                    }
                }
            }
        }

        handleLocale()
        handleAd()
    }

    private fun handleAd() {
        TapsellPlus.initialize(this, AdConstants.TAPSELL_KEY, object : TapsellPlusInitListener {
            override fun onInitializeSuccess(ads: AdNetworks?) {
                ads?.name?.log()
            }

            override fun onInitializeFailed(ads: AdNetworks?, error: AdNetworkError?) {
                error?.errorMessage?.log()
            }
        })
    }

    private fun handleLocale() {
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
    modifier: Modifier = Modifier,
    currentTheme: ThemeSetting
) {
    var theme by remember { mutableStateOf(currentTheme) }

    AppTheme(
        isDarkTheme = isDarkTheme(theme, isSystemInDarkTheme()),
        isDynamicColor = theme == ThemeSetting.System
    ) {
        val navController = rememberNavController()
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = Nav.Routes.home
        ) {
            composable(Nav.Routes.home) {
                HomeContent(
                    onNavigateTo = { navigationItem ->
                        when (navigationItem) {
                            NavigationItem.Home -> {}
                            NavigationItem.NewChat -> {
                                navController.navigate("${Nav.Routes.chat}/-1")
                            }
                            NavigationItem.History -> {
                                navController.navigate(Nav.Routes.history)
                            }
                            NavigationItem.Settings -> {
                                navController.navigate(Nav.Routes.settings)
                            }
                            NavigationItem.About -> {
                                navController.navigate(Nav.Routes.about)
                            }
                            NavigationItem.Images -> {
                                navController.navigate(Nav.Routes.images)
                            }
                        }
                    })
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
    if (themeSetting == ThemeSetting.Dark) return true
    return false
}