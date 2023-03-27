package io.github.rayangroup.hamyar.model.response

import com.squareup.moshi.Json
import io.github.rayangroup.hamyar.model.Chat

data class ChatCompletionChoice(
    val message: Chat,
    val index: Int,
    val logprobs: Int?,
    @field:Json(name = "finish_reason")
    val finishReason: String
)