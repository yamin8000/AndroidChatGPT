package io.github.aryantech.androidchatgpt.content.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class HistoryState(

) {
    init {

    }

    private suspend fun getAllHistories() {

    }
}

@Composable
fun rememberHistoryState(

) = remember() {
    HistoryState()
}