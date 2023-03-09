package io.github.aryantech.androidchatgpt.content.settings

import android.content.Context
import android.util.Log
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
import io.github.aryantech.androidchatgpt.content.modelsDataStore
import io.github.aryantech.androidchatgpt.content.settingsDataStore
import io.github.aryantech.androidchatgpt.util.Constants
import io.github.aryantech.androidchatgpt.util.DataStoreHelper
import io.github.aryantech.androidchatgpt.web.APIs
import io.github.aryantech.androidchatgpt.web.Web
import io.github.aryantech.androidchatgpt.web.Web.apiOf
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
            context.modelsDataStore.data.collect { prefs ->
                apiModels.value = prefs.asMap().values.map { it.toString() }
            }
            if (apiModels.value.isEmpty())
                apiModels.value = getModelsFromApi()
        }
    }

    private suspend fun getModelsFromApi(): List<String> {
        return Web.getRetrofit()
            .apiOf<APIs.ModelsAPIs>()
            .getAllModels()
            .data
            .map { it.obj }
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
}

@Composable
fun rememberSettingsState(
    context: Context = LocalContext.current,
    coroutineScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    themeSetting: MutableState<ThemeSetting> = rememberSaveable { mutableStateOf(ThemeSetting.System) },
    apiModel: MutableState<String> = rememberSaveable { mutableStateOf(Constants.DEFAULT_API_MODEL) },
    apiModels: MutableState<List<String>> = rememberSaveable { mutableStateOf(listOf()) }
) = remember(context, coroutineScope, themeSetting, apiModel, apiModels) {
    SettingsState(context, coroutineScope, themeSetting, apiModel, apiModels)
}