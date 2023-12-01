/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     SettingsState.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     SettingsState.kt Last modified at 2023/12/1
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

package io.github.yamin8000.chatgpt.content.settings

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.chatgpt.content.ThemeSetting
import io.github.yamin8000.chatgpt.content.main.settingsDataStore
import io.github.yamin8000.chatgpt.util.Constants
import io.github.yamin8000.chatgpt.util.DataStoreHelper
import io.github.yamin8000.chatgpt.util.log
import io.github.yamin8000.chatgpt.web.APIs
import io.github.yamin8000.chatgpt.web.Web
import io.github.yamin8000.chatgpt.web.Web.apiOf
import kotlinx.coroutines.launch

class SettingsState(
    context: Context,
    val scope: LifecycleCoroutineScope,
    val themeSetting: MutableState<ThemeSetting>,
    val apiModel: MutableState<String>,
    val apiModels: MutableState<List<String>>
) {
    private val settings = DataStoreHelper(context.settingsDataStore)

    init {
        scope.launch {
            themeSetting.value = ThemeSetting.valueOf(
                settings.getString(Constants.THEME) ?: ThemeSetting.System.name
            )
            apiModel.value = settings.getString(Constants.API_MODEL) ?: Constants.DEFAULT_API_MODEL
            apiModels.value = settings.getStringSet(Constants.API_MODELS)?.toList() ?: listOf()
            if (apiModels.value.isEmpty()) {
                apiModels.value = getModelsFromApi()
                updateApiModels(apiModels.value)
            }
        }
    }

    private suspend fun getModelsFromApi() = try {
        Web.getRetrofit()
            .apiOf<APIs.ModelsAPIs>()
            .getAllModels()
            .data
            .map { it.id }
    } catch (e: Exception) {
        log(e)
        listOf()
    }

    suspend fun updateThemeSetting(
        newTheme: ThemeSetting
    ) {
        themeSetting.value = newTheme
        settings.setString(Constants.THEME, newTheme.name)
    }

    suspend fun updateApiModel(
        newModel: String
    ) {
        apiModel.value = newModel
        settings.setString(Constants.API_MODEL, newModel)
    }

    private suspend fun updateApiModels(
        models: List<String>
    ) {
        apiModels.value = models
        settings.setStringSet(Constants.API_MODELS, models.toSet())
    }
}

@Composable
fun rememberSettingsState(
    context: Context = LocalContext.current,
    coroutineScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    themeSetting: MutableState<ThemeSetting> = rememberSaveable { mutableStateOf(ThemeSetting.System) },
    apiModel: MutableState<String> = rememberSaveable { mutableStateOf(Constants.DEFAULT_API_MODEL) },
    apiModels: MutableState<List<String>> = rememberSaveable { mutableStateOf(listOf()) }
) = remember(context, coroutineScope, themeSetting, apiModel, apiModels) {
    SettingsState(
        context,
        coroutineScope,
        themeSetting,
        apiModel,
        apiModels
    )
}