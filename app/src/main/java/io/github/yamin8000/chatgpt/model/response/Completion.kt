package io.github.yamin8000.chatgpt.model.response

import com.squareup.moshi.Json

data class Completion(
    val id: String,
    @field:Json(name = "object")
    val obj: String,
    val created: Long,
    val model: String,
    val choices: List<CompletionChoice>,
    val usage: Usage
)
