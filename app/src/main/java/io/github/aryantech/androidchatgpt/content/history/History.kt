package io.github.aryantech.androidchatgpt.content.history

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.aryantech.androidchatgpt.R
import io.github.aryantech.androidchatgpt.ui.composables.ScaffoldWithTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryContent(
    onBackClick: () -> Unit
) {
    val state = rememberHistoryState()

    ScaffoldWithTitle(
        title = stringResource(id = R.string.history),
        onBackClick = onBackClick,
        content = {
            LazyColumn(
                content = {
                    items() {

                    }
                }
            )
        }
    )
}