package io.github.yamin8000.chatgpt.model.response

import com.squareup.moshi.Json
import io.github.yamin8000.chatgpt.model.Chat

data class ChatCompletionChoice(
    val message: Chat,
    val index: Int,
    val logprobs: Int?,
    @field:Json(name = "finish_reason")
    val finishReason: String
)