/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     Settings.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     Settings.kt Last modified at 2023/12/1
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

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Dataset
import androidx.compose.material.icons.twotone.DisplaySettings
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.yamin8000.chatgpt.R
import io.github.yamin8000.chatgpt.content.ThemeSetting
import io.github.yamin8000.chatgpt.ui.composables.PersianText
import io.github.yamin8000.chatgpt.ui.composables.ScaffoldWithTitle
import io.github.yamin8000.chatgpt.ui.composables.SettingChangerDialog
import io.github.yamin8000.chatgpt.ui.composables.SettingsItem
import io.github.yamin8000.chatgpt.ui.composables.SettingsItemCard
import io.github.yamin8000.chatgpt.util.Constants
import io.github.yamin8000.chatgpt.util.LanguageUtils.setDefault
import io.github.yamin8000.chatgpt.util.LanguageUtils.toList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    onThemeChanged: (ThemeSetting) -> Unit,
    onBackClick: () -> Unit
) {
    val state = rememberSettingsState()

    ScaffoldWithTitle(
        title = stringResource(R.string.settings),
        onBackClick = onBackClick
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { LanguageSelectionSetting() }
            item {
                ThemeSetting(state.themeSetting.value) { newTheme ->
                    state.scope.launch { state.updateThemeSetting(newTheme) }
                    onThemeChanged(newTheme)
                }
            }
            item {
                ApiModelSetting(
                    apiModel = state.apiModel.value,
                    apiModels = state.apiModels.value,
                    onApiModelChange = { state.scope.launch { state.updateApiModel(it) } }
                )
            }
        }
    }
}

@Composable
fun LanguageSelectionSetting() {
    val language = stringResource(R.string.language)
    val locales = AppCompatDelegate.getApplicationLocales().toList()

    var isShowingDialog by remember { mutableStateOf(false) }

    SettingChangerDialog(
        isEnabled = isShowingDialog,
        title = language,
        options = locales,
        currentSetting = locales.first(),
        onDismiss = { isShowingDialog = false },
        displayProvider = { it.displayLanguage },
        onSettingChange = { AppCompatDelegate.setApplicationLocales(it.setDefault()) },
        icon = { Icon(imageVector = Icons.TwoTone.Language, contentDescription = language) }
    )

    SettingsItemCard(
        title = language,
        content = {
            SettingsItem(
                onClick = { isShowingDialog = true },
                content = {
                    Icon(imageVector = Icons.TwoTone.Language, contentDescription = language)
                    PersianText(locales.first().displayLanguage)
                }
            )
        }
    )
}

@Composable
fun ThemeSetting(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit
) {
    val theme = stringResource(R.string.theme)

    var isShowingThemeDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    SettingChangerDialog(
        isEnabled = isShowingThemeDialog,
        title = theme,
        options = ThemeSetting.entries,
        currentSetting = currentTheme,
        onSettingChange = onCurrentThemeChange,
        onDismiss = { isShowingThemeDialog = false },
        displayProvider = { context.getString(it.persianNameStringResource) },
        icon = { Icon(imageVector = Icons.TwoTone.DisplaySettings, contentDescription = theme) },
    )

    SettingsItemCard(
        title = theme,
        content = {
            SettingsItem(
                onClick = { isShowingThemeDialog = true },
                content = {
                    Icon(imageVector = Icons.TwoTone.DisplaySettings, contentDescription = theme)
                    PersianText(stringResource(currentTheme.persianNameStringResource))
                }
            )
            if (currentTheme == ThemeSetting.System && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                DynamicThemeNotice()
        }
    )
}

@Composable
fun ApiModelSetting(
    apiModel: String,
    apiModels: List<String>,
    onApiModelChange: (String) -> Unit
) {
    val dataSet = stringResource(R.string.apiModel)

    var isShowingDialog by remember { mutableStateOf(false) }

    SettingChangerDialog(
        isEnabled = isShowingDialog,
        title = dataSet,
        options = apiModels.sorted(),
        currentSetting = apiModel,
        onSettingChange = onApiModelChange,
        onDismiss = { isShowingDialog = false },
        icon = { Icon(imageVector = Icons.TwoTone.Dataset, contentDescription = dataSet) }
    )
    SettingsItemCard(
        title = dataSet,
        content = {
            SettingsItem(
                onClick = { isShowingDialog = true && apiModels.isNotEmpty() },
                content = {
                    Icon(imageVector = Icons.TwoTone.Dataset, contentDescription = dataSet)
                    PersianText(apiModel)
                }
            )
            Button(
                onClick = { onApiModelChange(Constants.CHAT_MODELS.first()) },
                content = { PersianText(stringResource(R.string.change_it_to_default)) },
                enabled = apiModels.isNotEmpty()
            )
            if (apiModel !in Constants.CHAT_MODELS) {
                PersianText(
                    modifier = Modifier.fillMaxWidth(),
                    text = buildString {
                        append(stringResource(R.string.suggested_model_for_chat))
                        append("\n")
                        Constants.CHAT_MODELS.forEach {
                            append("${it}\n")
                        }
                        trim()
                    }
                )
            }
        }
    )
}

@Composable
fun DynamicThemeNotice() {
    PersianText(
        text = stringResource(R.string.dynamic_theme_notice),
        textAlign = TextAlign.Justify
    )
}