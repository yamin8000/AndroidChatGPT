package io.github.aryantech.androidchatgpt.content.chat

import android.content.Context
import androidx.compose.material.SnackbarHost
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
import io.github.aryantech.androidchatgpt.R
import io.github.aryantech.androidchatgpt.content.home.HomeState
import io.github.aryantech.androidchatgpt.content.settingsDataStore
import io.github.aryantech.androidchatgpt.model.Chat
import io.github.aryantech.androidchatgpt.model.request.CreateChatCompletion
import io.github.aryantech.androidchatgpt.model.response.ChatCompletion
import io.github.aryantech.androidchatgpt.util.Constants
import io.github.aryantech.androidchatgpt.web.APIs
import io.github.aryantech.androidchatgpt.web.Web
import io.github.aryantech.androidchatgpt.web.Web.apiOf
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatState(
    val chatInput: MutableState<String>,
    val chat: MutableState<List<Chat>>,
    val model: MutableState<String>,
    context: Context,
    private val focusManager: FocusManager,
    private val scope: LifecycleCoroutineScope
) {
    private val retrofit = Web.getRetrofit()

    private val modelNotSupported = context.getString(R.string.model_not_supported)

    private val isModelSupported: Boolean
        get() = model.value in Constants.CHAT_MODELS

    var isWaitingForResponse = false

    var isInputAllowed = true

    val snackbarHost = SnackbarHostState()

    init {
        scope.launch {
            model.value = context.settingsDataStore.data.map {
                it[stringPreferencesKey(Constants.API_MODEL)]
            }.firstOrNull() ?: Constants.API_MODEL
        }
    }

    fun newChatInput(
        chatInput: String
    ) {
        if (isModelSupported) createNewChatInputRequest(chatInput)
        else scope.launch { snackbarHost.showSnackbar(modelNotSupported) }
    }

    private fun createNewChatInputRequest(
        chatInput: String
    ) {
        resetInput()
        chat.value = chat.value + Chat(role = "user", content = chatInput)

        scope.launch {
            isWaitingForResponse = true
            isInputAllowed = false
            val output = retrofit.apiOf<APIs.ChatCompletionsAPIs>().createChatCompletions(
                CreateChatCompletion(
                    model = model.value,
                    messages = chat.value
                )
            )
            isWaitingForResponse = false
            isInputAllowed = true
            updateChat(output.choices.first().message)
        }
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
}

@Composable
fun rememberChatState(
    chatInput: MutableState<String> = rememberSaveable { mutableStateOf("") },
    chat: MutableState<List<Chat>> = remember { mutableStateOf(listOf()) },
    model: MutableState<String> = rememberSaveable { mutableStateOf(Constants.DEFAULT_API_MODEL) },
    context: Context = LocalContext.current,
    focusManager: FocusManager = LocalFocusManager.current,
    scope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope
) = remember(chatInput, chat, model, context, focusManager, scope) {
    ChatState(chatInput, chat, model, context, focusManager, scope)
}