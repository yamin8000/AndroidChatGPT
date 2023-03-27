package io.github.rayan_group.hamyar.content.chat

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.rayan_group.hamyar.R
import io.github.rayan_group.hamyar.content.settingsDataStore
import io.github.rayan_group.hamyar.db.entity.HistoryEntity
import io.github.rayan_group.hamyar.db.entity.HistoryItemEntity
import io.github.rayan_group.hamyar.model.Chat
import io.github.rayan_group.hamyar.model.request.CreateChatCompletion
import io.github.rayan_group.hamyar.model.response.ChatCompletion
import io.github.rayan_group.hamyar.util.Constants
import io.github.rayan_group.hamyar.util.Constants.db
import io.github.rayan_group.hamyar.util.DateTimeUtils
import io.github.rayan_group.hamyar.util.log
import io.github.rayan_group.hamyar.web.APIs
import io.github.rayan_group.hamyar.web.Web
import io.github.rayan_group.hamyar.web.Web.apiOf
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatState(
    val chatInput: MutableState<String>,
    val chat: MutableState<List<Chat>>,
    val model: MutableState<String>,
    val title: MutableState<String>,
    val isOnline: MutableState<Boolean>,
    val isWaitingForResponse: MutableState<Boolean>,
    private val historyId: MutableState<String?>,
    private val context: Context,
    private val focusManager: FocusManager,
    val scope: LifecycleCoroutineScope
) {
    private val retrofit = Web.getRetrofit()

    private val modelNotSupported = context.getString(R.string.model_not_supported)

    private val isModelSupported: Boolean
        get() = model.value in Constants.CHAT_MODELS

    private var isSaveable = true

    private var isUpdatable = false

    var isInputAllowed = true

    val snackbarHost = SnackbarHostState()

    init {
        scope.launch {
            model.value = context.settingsDataStore.data.map {
                it[stringPreferencesKey(Constants.API_MODEL)]
            }.firstOrNull() ?: Constants.DEFAULT_API_MODEL

            if (historyId.value != null && historyId.value?.toLong() != -1L)
                handleHistoryLoading()
        }
    }

    private suspend fun handleHistoryLoading() {
        isSaveable = false
        isUpdatable = true
        historyId.value?.let {
            chat.value = loadFromHistory(it.toLong())
            title.value = db.historyDao().getById(it.toLong())?.title ?: ""
        }
    }

    suspend fun newChatInput(
        chatInput: String
    ) {
        if (isModelSupported) createNewChatInputRequest(chatInput)
        else snackbarHost.showSnackbar(modelNotSupported)
    }

    private suspend fun createNewChatInputRequest(
        chatInput: String
    ) {
        resetInput()
        chat.value = chat.value + Chat(role = "user", content = chatInput.trim())

        isWaitingForResponse.value = true
        isInputAllowed = false
        val output = try {
            retrofit.apiOf<APIs.ChatCompletionsAPIs>().createChatCompletions(
                CreateChatCompletion(
                    model = model.value,
                    messages = chat.value
                )
            )
        } catch (e: Exception) {
            handleError(e)
            null
        }
        isWaitingForResponse.value = false
        isInputAllowed = true
        if (output != null) {
            updateChat(output.choices.first().message)
            if (chat.value.size > 3)
                predictTitle()
        }
    }

    private fun handleError(
        exception: Exception
    ) {
        exception.stackTraceToString().log()
    }

    private fun updateChat(
        chatItem: Chat
    ) {
        chat.value = chat.value + chatItem
    }

    private fun resetInput() {
        this.chatInput.value = ""
        focusManager.clearFocus()
    }

    suspend fun handleHistorySaving() {
        if (chat.value.isNotEmpty()) {
            if (isSaveable) saveToHistory()
            if (isUpdatable) updateHistory()
        }
    }

    private fun updateHistory() {

    }

    private suspend fun saveToHistory() {
        val historyTitle = createHistoryTitle(title.value)
        val history = HistoryEntity(title = historyTitle, date = DateTimeUtils.zonedNow())
        val id = db.historyDao().insert(history)
        chat.value.forEach { chatItem -> addItemToHistory(chatItem, id) }
    }

    private suspend fun addItemToHistory(
        chatItem: Chat,
        id: Long
    ) {
        val item = HistoryItemEntity(
            content = chatItem.content,
            owner = ChatBubbleOwner.of(chatItem.role),
            historyId = id
        )
        db.historyItemDao().insert(item)
    }

    private fun createHistoryTitle(
        predictedTitle: String
    ): String {
        val first = chat.value.first().content
        return predictedTitle.ifBlank { first }
    }

    private suspend fun loadFromHistory(
        historyId: Long
    ) = db.historyItemDao()
        .getByParam("historyId", historyId)
        .map { Chat(it.owner.toString().lowercase(), it.content) }

    private suspend fun predictTitle() {
        var titlePrediction: ChatCompletion? = null
        try {
            titlePrediction = Web.getRetrofit()
                .apiOf<APIs.ChatCompletionsAPIs>()
                .createChatCompletions(
                    CreateChatCompletion(
                        model = model.value,
                        messages = listOf(
                            Chat(
                                role = "user",
                                content = assembleChatForPrediction()
                            )
                        )
                    )
                )
        } catch (e: Exception) {
            //ignored
        }
        if (titlePrediction != null) {
            title.value = titlePrediction.choices
                .first()
                .message
                .content
                .trim()
                .replace("\\n", "")
        }
    }

    private fun assembleChatForPrediction(): String {
        return buildString {
            append(Constants.TITLE_PREDICTION_PROMPT)
            chat.value.forEach { message ->
                append(message.content)
                append("\n")
            }
        }
    }
}

@Composable
fun rememberChatState(
    chatInput: MutableState<String> = rememberSaveable { mutableStateOf("") },
    chat: MutableState<List<Chat>> = rememberSaveable { mutableStateOf(listOf()) },
    model: MutableState<String> = rememberSaveable { mutableStateOf(Constants.DEFAULT_API_MODEL) },
    title: MutableState<String> = rememberSaveable { mutableStateOf("") },
    isOnline: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) },
    isWaitingForResponse: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    historyId: MutableState<String?> = rememberSaveable { mutableStateOf(null) },
    context: Context = LocalContext.current,
    focusManager: FocusManager = LocalFocusManager.current,
    scope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope
) = remember(
    chatInput,
    chat,
    model,
    title,
    isOnline,
    isWaitingForResponse,
    historyId,
    context,
    focusManager,
    scope
) {
    ChatState(
        chatInput,
        chat,
        model,
        title,
        isOnline,
        isWaitingForResponse,
        historyId,
        context,
        focusManager,
        scope
    )
}