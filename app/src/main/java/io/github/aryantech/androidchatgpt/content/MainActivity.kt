package io.github.aryantech.androidchatgpt.content

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
import io.github.aryantech.androidchatgpt.ad.AdConstants
import io.github.aryantech.androidchatgpt.ad.TapsellAdContent
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
import io.github.aryantech.androidchatgpt.util.log
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

        scope.launch {
            db = createDb()
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
                        TapsellAdContent(
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth(),
                            onCreated = { adView = it },
                            onUpdate = { adView = it }
                        )
                        MainContent(
                            currentTheme = theme,
                            modifier = Modifier.weight(1f)
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