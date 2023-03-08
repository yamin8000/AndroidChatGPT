package io.github.aryantech.androidchatgpt.content.settings

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
import io.github.aryantech.androidchatgpt.content.ThemeSetting
import io.github.aryantech.androidchatgpt.content.settingsDataStore
import io.github.aryantech.androidchatgpt.util.Constants
import io.github.aryantech.androidchatgpt.util.DataStoreHelper
import kotlinx.coroutines.launch

class SettingsState(
    context: Context,
    val scope: LifecycleCoroutineScope,
    val themeSetting: MutableState<ThemeSetting>,
) {
    private val dataStore = DataStoreHelper(context.settingsDataStore)

    init {
        scope.launch {
            themeSetting.value = ThemeSetting.valueOf(
                dataStore.getString(Constants.THEME) ?: ThemeSetting.System.name
            )
        }
    }

    suspend fun updateThemeSetting(
        newTheme: ThemeSetting
    ) {
        themeSetting.value = newTheme
        dataStore.setString(Constants.THEME, newTheme.name)
    }
}

@Composable
fun rememberSettingsState(
    context: Context = LocalContext.current,
    coroutineScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    themeSetting: MutableState<ThemeSetting> = rememberSaveable { mutableStateOf(ThemeSetting.System) }
) = remember(context, coroutineScope, themeSetting) {
    SettingsState(context, coroutineScope, themeSetting)
}