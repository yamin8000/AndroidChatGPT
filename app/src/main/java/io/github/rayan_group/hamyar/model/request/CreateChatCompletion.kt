package io.github.rayan_group.hamyar.model.request

import com.squareup.moshi.Json
import io.github.rayan_group.hamyar.model.Chat

data class CreateChatCompletion(
    val model: String,
    val messages: List<Chat>,
    val temperature: Int? = 1,
    @field:Json(name = "top_p")
    val topP: Int? = 1,
    val n: Int? = 1,
    val stream: Boolean? = false,
    val stop: List<String>? = null,
    @field:Json(name = "max_tokens")
    val maxTokens: Int? = null,
    @field:Json(name = "presence_penalty")
    val presencePenalty: Float? = 0f,
    @field:Json(name = "frequency_penalty")
    val frequencyPenalty: Float? = 0f,
    @Suppress("SpellCheckingInspection")
    @field:Json(name = "logit_bias")
    val logitBias: Map<String, Int>? = null,
    val user: String? = null
)
