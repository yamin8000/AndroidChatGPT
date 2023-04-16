package io.github.rayangroup.hamyar.content.chat

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.rayangroup.hamyar.R
import io.github.rayangroup.hamyar.content.main.settingsDataStore
import io.github.rayangroup.hamyar.db.entity.HistoryEntity
import io.github.rayangroup.hamyar.db.entity.HistoryItemEntity
import io.github.rayangroup.hamyar.model.Chat
import io.github.rayangroup.hamyar.model.request.CreateChatCompletion
import io.github.rayangroup.hamyar.util.Constants
import io.github.rayangroup.hamyar.util.Constants.db
import io.github.rayangroup.hamyar.util.DataStoreHelper
import io.github.rayangroup.hamyar.util.DateTimeUtils
import io.github.rayangroup.hamyar.util.log
import io.github.rayangroup.hamyar.util.reportException
import io.github.rayangroup.hamyar.web.APIs
import io.github.rayangroup.hamyar.web.Web
import io.github.rayangroup.hamyar.web.Web.apiOf
import kotlinx.coroutines.launch

class ChatState(
    val chatInput: MutableState<String>,
    val chat: MutableState<List<Chat>>,
    val model: MutableState<String>,
    val title: MutableState<String>,
    val isOnline: MutableState<Boolean>,
    val isWaitingForResponse: MutableState<Boolean>,
    private val historyId: MutableState<Long>,
    context: Context,
    val scope: LifecycleCoroutineScope
) {
    private val retrofit = Web.getRetrofit()

    private val modelNotSupported = context.getString(R.string.model_not_supported)

    private val aiFailedToAnswer = context.getString(R.string.ai_failed_answering)

    private val isModelSupported: Boolean
        get() = model.value in Constants.CHAT_MODELS

    private var isUpdatable = false

    val snackbarHost = SnackbarHostState()

    private val settings = DataStoreHelper(context.settingsDataStore)

    private var historyChat = listOf<Chat>()

    init {
        scope.launch {
            model.value = settings.getString(Constants.API_MODEL) ?: Constants.DEFAULT_API_MODEL

            if (historyId.value != -1L)
                handleHistoryLoading()
        }
    }

    private suspend fun createNewHistoryEntity() {
        historyId.value = db.historyDao().insert(
            HistoryEntity(title.value, DateTimeUtils.zonedNow())
        )
    }

    private suspend fun handleHistoryLoading() {
        isUpdatable = true
        chat.value = loadFromHistory(historyId.value)
        historyChat = chat.value
        title.value = db.historyDao().getById(historyId.value)?.title ?: ""
    }

    suspend fun newChatHandler(
        chatInput: String
    ) {
        if (isModelSupported) {
            val newChat = Chat(role = "user", content = chatInput.trim())
            chat.value += newChat
            if (chat.value.size == 1)
                createNewHistoryEntity()
            addChatItemToHistory(newChat, historyId.value)

            resetInput()
            changeInputAllowance(false)
            val aiAnswer = chatCompletionRequest()
                ?.choices
                ?.first()
                ?.message ?: Chat(role = "assistant", content = aiFailedToAnswer)
            changeInputAllowance(true)

            chat.value += aiAnswer
            if (chat.value.size > 3)
                title.value = createHistoryTitle(predictTitle())
            addChatItemToHistory(aiAnswer, historyId.value)
            updateChatHistoryTitle()
        } else snackbarHost.showSnackbar(modelNotSupported)
    }

    private suspend fun chatCompletionRequest() = try {
        retrofit.apiOf<APIs.ChatCompletionsAPIs>().createChatCompletions(
            CreateChatCompletion(
                model = model.value,
                messages = chat.value
            )
        )
    } catch (e: Exception) {
        log(e)
        reportException(e)
        null
    }

    private fun resetInput() {
        this.chatInput.value = ""
    }

    private fun changeInputAllowance(
        isAllowed: Boolean
    ) {
        isWaitingForResponse.value = !isAllowed
    }

    private suspend fun addChatItemToHistory(
        chat: Chat,
        id: Long
    ): Long {
        updateChatHistoryTitle()
        return db.historyItemDao().insert(
            HistoryItemEntity(
                content = chat.content,
                owner = ChatBubbleOwner.of(chat.role),
                historyId = id
            )
        )
    }

    private suspend fun updateChatHistoryTitle(): Int? {
        val dao = db.historyDao()
        return dao
            .getById(historyId.value)?.let { historyEntity ->
                dao.update(
                    historyEntity.copy(title = title.value, date = DateTimeUtils.zonedNow())
                )
            }
    }

    private fun createHistoryTitle(
        predictedTitle: String
    ): String = predictedTitle.ifBlank { chat.value.first().content }

    private suspend fun loadFromHistory(
        historyId: Long
    ) = db.historyItemDao()
        .getByParam("historyId", historyId)
        .map { Chat(it.owner.toString().lowercase(), it.content) }

    private suspend fun predictTitle() = try {
        Web.getRetrofit()
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
            ).choices.first().message.content.trim()
            .replace("\\n", "")
            .removeSurrounding("\"")
    } catch (e: Exception) {
        log(e)
        reportException(e)
        title.value
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
    historyId: MutableState<Long> = rememberSaveable { mutableStateOf(-1L) },
    context: Context = LocalContext.current,
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
        scope
    )
}