package io.github.aryantech.androidchatgpt.content.settings

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DisplaySettings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.aryantech.androidchatgpt.R
import io.github.aryantech.androidchatgpt.content.ThemeSetting
import io.github.aryantech.androidchatgpt.ui.composables.PersianText
import io.github.aryantech.androidchatgpt.ui.composables.ScaffoldWithTitle
import io.github.aryantech.androidchatgpt.ui.composables.SettingsItem
import io.github.aryantech.androidchatgpt.ui.composables.SettingsItemCard
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
        }
    }
}

@Composable
fun ThemeSetting(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit
) {
    var isShowingThemeDialog by remember { mutableStateOf(false) }

    SettingsItemCard(
        title = stringResource(R.string.theme)
    ) {
        if (isShowingThemeDialog) {
            ThemeChangerDialog(
                currentTheme = currentTheme,
                onCurrentThemeChange = onCurrentThemeChange,
                onDismiss = { isShowingThemeDialog = false }
            )
        }
        SettingsItem(
            onClick = { isShowingThemeDialog = true },
            content = {
                Icon(
                    imageVector = Icons.TwoTone.DisplaySettings,
                    contentDescription = stringResource(R.string.theme)
                )
                PersianText(
                    text = stringResource(currentTheme.persianNameStringResource),
                    modifier = Modifier.padding()
                )
            }
        )
        if (currentTheme == ThemeSetting.System && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            DynamicThemeNotice()
    }
}

@Composable
fun ThemeChangerDialog(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit,
    onDismiss: () -> Unit
) {
    val themes = remember { ThemeSetting.values() }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { /*ignored*/ },
        title = { PersianText(stringResource(R.string.theme)) },
        icon = { Icon(imageVector = Icons.TwoTone.DisplaySettings, contentDescription = null) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(16.dp)
                    .selectableGroup()
                    .fillMaxWidth()
            ) {
                themes.forEach { theme ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (theme == currentTheme),
                                role = Role.RadioButton,
                                onClick = {
                                    onCurrentThemeChange(theme)
                                    onDismiss()
                                }
                            )
                    ) {
                        RadioButton(
                            selected = (theme == currentTheme),
                            onClick = null,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        PersianText(
                            text = stringResource(theme.persianNameStringResource),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
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