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
import androidx.compose.ui.unit.dp
import io.github.aryantech.androidchatgpt.util.log
import io.github.aryantech.androidchatgpt.web.APIs
import io.github.aryantech.androidchatgpt.web.ApiKey
import io.github.aryantech.androidchatgpt.web.Web
import io.github.aryantech.androidchatgpt.web.Web.apiOf
import okhttp3.*
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    onSettingsClick: () -> Unit
) {
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
                    Text("Hello there!")
                    Button(
                        onClick = {},
                        content = { Text("OK") },
                    )
                }
            }
        )
    }
}