package io.github.aryantech.androidchatgpt.content.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.aryantech.androidchatgpt.R
import io.github.aryantech.androidchatgpt.content.ThemeSetting
import io.github.aryantech.androidchatgpt.ui.composables.ScaffoldWithTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    onThemeChanged: (ThemeSetting) -> Unit,
    onBackClick: () -> Unit
) {
    ScaffoldWithTitle(
        title = stringResource(R.string.settings),
        onBackClick = onBackClick
    ) {

    }
}