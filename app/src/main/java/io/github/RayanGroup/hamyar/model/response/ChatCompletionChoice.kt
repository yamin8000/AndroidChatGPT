package io.github.RayanGroup.hamyar.model.response

import com.squareup.moshi.Json
import io.github.RayanGroup.hamyar.model.Chat

data class ChatCompletionChoice(
    val message: Chat,
    val index: Int,
    val logprobs: Int?,
    @field:Json(name = "finish_reason")
    val finishReason: String
)