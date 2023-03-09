package io.github.aryantech.androidchatgpt.content.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import io.github.aryantech.androidchatgpt.util.Constants
import io.github.aryantech.androidchatgpt.web.APIs
import io.github.aryantech.androidchatgpt.web.Web
import io.github.aryantech.androidchatgpt.web.Web.apiOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    onSettingsClick: () -> Unit
) {
    Log.d(Constants.LOG_TAG, "Hello there!")
    var models = listOf<String>()
    LaunchedEffect(Unit) {
        models = Web.getRetrofit().apiOf<APIs.ModelsAPIs>().getAllModels().data.map { it.obj }
        Log.d(Constants.LOG_TAG, "Hello there!")
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MainTopAppBar(
                    scrollBehavior = scrollBehavior,
                    onSettingsClick = onSettingsClick
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(8.dp)
                ) {
                    Text(models.toString())
                    Button(
                        onClick = {},
                        content = { Text("OK") },
                    )
                }
            }
        )
    }
}