package io.github.aryantech.androidchatgpt.content.home

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
import io.github.aryantech.androidchatgpt.model.request.CreateCompletion
import io.github.aryantech.androidchatgpt.web.APIs
import io.github.aryantech.androidchatgpt.web.Web
import io.github.aryantech.androidchatgpt.web.Web.apiOf
import kotlinx.coroutines.launch

class HomeState(
    val chatInput: MutableState<String>,
    val chat: MutableState<List<String>>,
    private val focusManager: FocusManager,
    private val scope: LifecycleCoroutineScope
) {
    private val retrofit = Web.getRetrofit()
    fun newChatInput(
        chatInput: String
    ) {
        resetInput()
        chat.value = chat.value + chatInput

        scope.launch {
            val output = retrofit.apiOf<APIs.CompletionsAPIs>().createCompletion(
                CreateCompletion(
                    model = "text-davinci-003",
                    prompt = listOf(chatInput)
                )
            )
            chat.value = chat.value + output.choices.first().text
        }
    }

    private fun resetInput() {
        this.chatInput.value = ""
        focusManager.clearFocus()
    }
}

@Composable
fun rememberHomeState(
    chatInput: MutableState<String> = rememberSaveable { mutableStateOf("") },
    chat: MutableState<List<String>> = rememberSaveable { mutableStateOf(listOf()) },
    focusManager: FocusManager = LocalFocusManager.current,
    scope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope
) = remember(chatInput, chat, focusManager, scope) {
    HomeState(chatInput, chat, focusManager, scope)
}