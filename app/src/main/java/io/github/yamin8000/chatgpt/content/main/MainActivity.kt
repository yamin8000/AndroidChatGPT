/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     MainActivity.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     MainActivity.kt Last modified at 2023/12/1
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
import io.github.yamin8000.chatgpt.content.ThemeSetting
import io.github.yamin8000.chatgpt.db.AppDatabase
import io.github.yamin8000.chatgpt.ui.composables.MySnackbar
import io.github.yamin8000.chatgpt.ui.composables.PersianText
import io.github.yamin8000.chatgpt.util.Constants
import io.github.yamin8000.chatgpt.util.Constants.db
import io.github.yamin8000.chatgpt.util.Constants.isDbInitialized
import io.github.yamin8000.chatgpt.util.DataStoreHelper
import io.github.yamin8000.chatgpt.util.log
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