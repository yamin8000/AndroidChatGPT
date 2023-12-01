package io.github.rayangroup.hamyar.content.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import io.github.rayangroup.hamyar.content.ThemeSetting
import io.github.rayangroup.hamyar.db.AppDatabase
import io.github.rayangroup.hamyar.ui.composables.MySnackbar
import io.github.rayangroup.hamyar.ui.composables.PersianText
import io.github.rayangroup.hamyar.util.Constants
import io.github.rayangroup.hamyar.util.Constants.db
import io.github.rayangroup.hamyar.util.Constants.isDbInitialized
import io.github.rayangroup.hamyar.util.DataStoreHelper
import io.github.rayangroup.hamyar.util.log
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

        try {
            db = createDb()

            scope.launch {
                val theme = getCurrentTheme()
                setContent {
                    val snackbarHostState = remember { SnackbarHostState() }
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(snackbarHostState) { data ->
                                MySnackbar {
                                    PersianText(
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        text = data.visuals.message
                                    )
                                }
                            }
                        },
                    ) {
                        MainContent(currentTheme = theme)
                    }
                }
            }

            handleLocale()
        } catch (e: Exception) {
            log(e)
        }
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