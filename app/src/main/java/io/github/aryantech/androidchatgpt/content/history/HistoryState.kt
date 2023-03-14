package io.github.aryantech.androidchatgpt.content.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.aryantech.androidchatgpt.db.entity.HistoryEntity
import io.github.aryantech.androidchatgpt.util.Constants.db
import kotlinx.coroutines.launch

class HistoryState(
    scope: LifecycleCoroutineScope,
    val history: MutableState<List<HistoryEntity>>
) {
    init {
        scope.launch {
            history.value = getAllHistories()
        }
    }

    private suspend fun getAllHistories() = db.historyDao().getAll()
}

@Composable
fun rememberHistoryState(
    scope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    history: MutableState<List<HistoryEntity>> = remember { mutableStateOf(listOf()) }
) = remember(scope, history) {
    HistoryState(scope, history)
}