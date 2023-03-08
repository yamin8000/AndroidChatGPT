package io.github.aryantech.androidchatgpt.model.request

import com.squareup.moshi.Json

data class CreateCompletion(
    val model: String,
    val prompt: List<String>?,
    val suffix: String? = null,
    @field:Json(name = "max_tokens")
    val maxTokens: Int? = 16,
    val temperature: Float? = 1f,
    @field:Json(name = "top_p")
    val topP: Float? = 1f,
    val n: Int? = 1,
    val stream: Boolean? = false,
    val logprobs: Int? = null,
    val echo: Boolean? = false,
    val stop: List<String>? = null,
    @field:Json(name = "presence_penalty")
    val presencePenalty: Float? = 0f,
    @field:Json(name = "frequency_penalty")
    val frequencyPenalty: Float? = 0f,
    @field:Json(name = "best_of") val bestOf: Int? = 1,
    @Suppress("SpellCheckingInspection")
    @field:Json(name = "logit_bias")
    val logitBias: Map<String, Int>? = null,
    val user: String? = null
)
