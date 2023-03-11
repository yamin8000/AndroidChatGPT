package io.github.aryantech.androidchatgpt.content.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.aryantech.androidchatgpt.content.home.HomeState
import io.github.aryantech.androidchatgpt.model.Chat
import io.github.aryantech.androidchatgpt.model.request.CreateChatCompletion
import io.github.aryantech.androidchatgpt.web.APIs
import io.github.aryantech.androidchatgpt.web.Web
import io.github.aryantech.androidchatgpt.web.Web.apiOf
import kotlinx.coroutines.launch

class ChatState(
    val chatInput: MutableState<String>,
    val chat: MutableState<List<Chat>>,
    private val focusManager: FocusManager,
    private val scope: LifecycleCoroutineScope
) {
    private val retrofit = Web.getRetrofit()

    fun newChatInput(
        chatInput: String
    ) {
        resetInput()
        chat.value = chat.value + Chat(role = "user", content = chatInput)

        scope.launch {
            val output = retrofit.apiOf<APIs.ChatCompletionsAPIs>().createChatCompletions(
                CreateChatCompletion(
                    model = "gpt-3.5-turbo",
                    messages = chat.value
                )
            )
            chat.value = chat.value + output.choices.first().message
        }
    }

    private fun resetInput() {
        this.chatInput.value = ""
        focusManager.clearFocus()
    }
}

@Composable
fun rememberChatState(
    chatInput: MutableState<String> = rememberSaveable { mutableStateOf("") },
    chat: MutableState<List<Chat>> = rememberSaveable { mutableStateOf(listOf()) },
    focusManager: FocusManager = LocalFocusManager.current,
    scope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope
) = remember(chatInput, chat, focusManager, scope) {
    ChatState(chatInput, chat, focusManager, scope)
}