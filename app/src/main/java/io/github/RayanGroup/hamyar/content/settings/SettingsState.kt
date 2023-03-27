package io.github.RayanGroup.hamyar.content.settings

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
import io.github.RayanGroup.hamyar.content.ThemeSetting
import io.github.RayanGroup.hamyar.content.settingsDataStore
import io.github.RayanGroup.hamyar.util.Constants
import io.github.RayanGroup.hamyar.util.DataStoreHelper
import io.github.RayanGroup.hamyar.web.APIs
import io.github.RayanGroup.hamyar.web.Web
import io.github.RayanGroup.hamyar.web.Web.apiOf
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

    private suspend fun getModelsFromApi(): List<String> {
        return Web.getRetrofit()
            .apiOf<APIs.ModelsAPIs>()
            .getAllModels()
            .data
            .map { it.id }
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
    SettingsState(context, coroutineScope, themeSetting, apiModel, apiModels)
}