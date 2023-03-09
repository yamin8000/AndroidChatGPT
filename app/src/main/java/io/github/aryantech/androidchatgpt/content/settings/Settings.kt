package io.github.aryantech.androidchatgpt.content.settings

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DataArray
import androidx.compose.material.icons.twotone.DisplaySettings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.aryantech.androidchatgpt.R
import io.github.aryantech.androidchatgpt.content.ThemeSetting
import io.github.aryantech.androidchatgpt.ui.composables.*
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
fun ApiModelSetting(
    apiModel: String,
    apiModels: List<String>,
    onApiModelChange: (String) -> Unit
) {
    var isShowingDialog by remember { mutableStateOf(false) }
    SettingChangerDialog(
        isEnabled = isShowingDialog,
        title = stringResource(R.string.apiModel),
        options = apiModels,
        currentSetting = apiModel,
        onSettingChange = onApiModelChange,
        onDismiss = { isShowingDialog = false }
    )
    SettingsItemCard(
        title = stringResource(R.string.apiModel)
    ) {
        SettingsItem(
            onClick = { isShowingDialog = true },
            content = {
                Icon(
                    imageVector = Icons.TwoTone.DataArray,
                    contentDescription = stringResource(R.string.apiModel)
                )
                Text(apiModel)
            }
        )
    }
}

@Composable
fun ThemeSetting(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit
) {
    var isShowingThemeDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    SettingChangerDialog(
        isEnabled = isShowingThemeDialog,
        title = stringResource(R.string.theme),
        options = ThemeSetting.values().toList(),
        currentSetting = currentTheme,
        onSettingChange = onCurrentThemeChange,
        onDismiss = { isShowingThemeDialog = false },
        displayProvider = { context.getString(it.persianNameStringResource) }
    )

    SettingsItemCard(
        title = stringResource(R.string.theme)
    ) {
        SettingsItem(
            onClick = { isShowingThemeDialog = true },
            content = {
                Icon(
                    imageVector = Icons.TwoTone.DisplaySettings,
                    contentDescription = stringResource(R.string.theme)
                )
                PersianText(stringResource(currentTheme.persianNameStringResource))
            }
        )
        if (currentTheme == ThemeSetting.System && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            DynamicThemeNotice()
    }
}

@Composable
fun DynamicThemeNotice() {
    PersianText(
        text = stringResource(R.string.dynamic_theme_notice),
        textAlign = TextAlign.Justify
    )
}