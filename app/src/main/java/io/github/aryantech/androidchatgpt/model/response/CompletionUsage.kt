package io.github.aryantech.androidchatgpt.model.response

import com.squareup.moshi.Json

data class CompletionUsage(
    @field:Json(name = "prompt_tokens")
    val promptTokens: Int,
    @field:Json(name = "completion_tokens")
    val completionTokens: Int,
    @field:Json(name = "total_tokens")
    val totalTokens: Int
)
