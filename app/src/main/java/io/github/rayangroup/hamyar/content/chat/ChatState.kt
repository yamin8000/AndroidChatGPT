package io.github.rayangroup.hamyar.content.chat

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
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.rayangroup.hamyar.R
import io.github.rayangroup.hamyar.content.main.settingsDataStore
import io.github.rayangroup.hamyar.db.entity.HistoryEntity
import io.github.rayangroup.hamyar.db.entity.HistoryItemEntity
import io.github.rayangroup.hamyar.model.Chat
import io.github.rayangroup.hamyar.model.request.CreateChatCompletion
import io.github.rayangroup.hamyar.model.request.CreateEdit
import io.github.rayangroup.hamyar.model.response.ChatCompletion
import io.github.rayangroup.hamyar.util.Constants
import io.github.rayangroup.hamyar.util.Constants.db
import io.github.rayangroup.hamyar.util.DataStoreHelper
import io.github.rayangroup.hamyar.util.DateTimeUtils
import io.github.rayangroup.hamyar.util.log
import io.github.rayangroup.hamyar.web.APIs
import io.github.rayangroup.hamyar.web.Web
import io.github.rayangroup.hamyar.web.Web.apiOf
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ChatState(
    val chatInput: MutableState<String>,
    val chatInputSuggestion: MutableState<String>,
    val chat: MutableState<List<Chat>>,
    val model: MutableState<String>,
    val title: MutableState<String>,
    val isOnline: MutableState<Boolean>,
    val isWaitingForResponse: MutableState<Boolean>,
    private val historyId: MutableState<String?>,
    context: Context,
    private val focusManager: FocusManager,
    val scope: LifecycleCoroutineScope,
    val isExperimentalFeaturesOn: MutableState<Boolean>
) {
    private val retrofit = Web.getRetrofit()

    private val modelNotSupported = context.getString(R.string.model_not_supported)

    private val isModelSupported: Boolean
        get() = model.value in Constants.CHAT_MODELS

    private var isSaveable = true

    private var isUpdatable = false

    var isInputAllowed = true

    val snackbarHost = SnackbarHostState()

    private val settings = DataStoreHelper(context.settingsDataStore)

    private var historyChat = listOf<Chat>()

    init {
        scope.launch {
            model.value = settings.getString(Constants.API_MODEL) ?: Constants.DEFAULT_API_MODEL

            if (historyId.value != null && historyId.value?.toLong() != -1L)
                handleHistoryLoading()

            isExperimentalFeaturesOn.value =
                settings.getBool(Constants.EXPERIMENTAL_FEATURES_STATE) ?: false
        }
    }

    private suspend fun handleHistoryLoading() {
        isSaveable = false
        isUpdatable = true
        historyId.value?.let {
            chat.value = loadFromHistory(it.toLong())
            historyChat = chat.value
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
        } finally {
            isWaitingForResponse.value = false
            isInputAllowed = true
        }
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
        this.chatInputSuggestion.value = ""
        focusManager.clearFocus()
    }

    suspend fun handleHistorySaving() {
        if (chat.value.isNotEmpty()) {
            if (isSaveable)
                saveToHistory()
            if (isUpdatable && chat.value.size > historyChat.size)
                updateHistory()
        }
    }

    private suspend fun updateHistory() {
        historyId.value?.toLong()?.let { id ->
            db.historyDao().getById(id)?.let { history ->
                updateChatHistoryTitle(history)
                updateChatHistoryItems(id)
            }
        }
    }

    private suspend fun updateChatHistoryItems(
        id: Long
    ) {
        chat.value.takeLast(chat.value.size - historyChat.size).forEach { newChat ->
            updateSingleChatHistoryItem(newChat, id)
        }
    }

    private suspend fun updateSingleChatHistoryItem(
        newChat: Chat,
        id: Long
    ) {
        db.historyItemDao().insert(
            HistoryItemEntity(
                content = newChat.content,
                owner = ChatBubbleOwner.of(newChat.role),
                historyId = id
            )
        )
    }

    private suspend fun updateChatHistoryTitle(
        history: HistoryEntity
    ) {
        db.historyDao().update(
            history.copy(title = title.value, date = DateTimeUtils.zonedNow())
        )
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

    suspend fun suggestInput() {
        chatInputSuggestion.value = try {
            Web.getRetrofit().apiOf<APIs.EditsAPIs>().createEdit(
                createEdit = CreateEdit(
                    instruction = Constants.INPUT_EDIT_PROMPT,
                    input = chatInput.value
                )
            ).choices.first().text
        } catch (e: Exception) {
            (e as? HttpException)?.message?.log()
            ""
        }
    }
}

@Composable
fun rememberChatState(
    chatInput: MutableState<String> = rememberSaveable { mutableStateOf("") },
    chatInputSuggestion: MutableState<String> = rememberSaveable { mutableStateOf("") },
    chat: MutableState<List<Chat>> = rememberSaveable { mutableStateOf(listOf()) },
    model: MutableState<String> = rememberSaveable { mutableStateOf(Constants.DEFAULT_API_MODEL) },
    title: MutableState<String> = rememberSaveable { mutableStateOf("") },
    isOnline: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) },
    isWaitingForResponse: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    historyId: MutableState<String?> = rememberSaveable { mutableStateOf(null) },
    context: Context = LocalContext.current,
    focusManager: FocusManager = LocalFocusManager.current,
    scope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    isExperimentalFeaturesOn: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
) = remember(
    chatInput,
    chatInputSuggestion,
    chat,
    model,
    title,
    isOnline,
    isWaitingForResponse,
    historyId,
    context,
    focusManager,
    scope,
    isExperimentalFeaturesOn
) {
    ChatState(
        chatInput,
        chatInputSuggestion,
        chat,
        model,
        title,
        isOnline,
        isWaitingForResponse,
        historyId,
        context,
        focusManager,
        scope,
        isExperimentalFeaturesOn
    )
}