/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     HistoryState.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     HistoryState.kt Last modified at 2023/12/1
 *     This file is part of AndroidChatGPT/AndroidChatGPT.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     AndroidChatGPT/AndroidChatGPT.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     AndroidChatGPT/AndroidChatGPT.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with AndroidChatGPT.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.chatgpt.content.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.chatgpt.db.dao.DAOs
import io.github.yamin8000.chatgpt.db.entity.HistoryEntity
import io.github.yamin8000.chatgpt.util.Constants.db
import kotlinx.coroutines.launch

class HistoryState(
    private val scope: LifecycleCoroutineScope,
    val history: MutableState<List<HistoryEntity>>
) {
    init {
        scope.launch { refreshHistories() }
    }

    private suspend fun refreshHistories() {
        history.value = getAllHistories()
    }

    private suspend fun getAllHistories() = db.historyDao().getAll().sortedByDescending { it.date }

    fun edit(
        id: Long,
        newTitle: String
    ) = scope.launch {
        crudAction(id) { item, dao ->
            dao.update(item.copy(title = newTitle.trim()))
        }
    }

    fun delete(
        id: Long
    ) = scope.launch {
        crudAction(id) { item, dao -> dao.delete(item) }
    }

    private suspend fun crudAction(
        id: Long,
        action: suspend (HistoryEntity, DAOs.HistoryDao) -> Unit
    ) {
        val dao = db.historyDao()
        val item = dao.getById(id)
        if (item != null) action(item, dao)
        refreshHistories()
    }
}

@Composable
fun rememberHistoryState(
    scope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    history: MutableState<List<HistoryEntity>> = remember { mutableStateOf(listOf()) }
) = remember(scope, history) {
    HistoryState(scope, history)
}