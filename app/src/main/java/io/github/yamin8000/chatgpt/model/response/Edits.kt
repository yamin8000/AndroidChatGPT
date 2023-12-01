package io.github.yamin8000.chatgpt.model.response

import com.squareup.moshi.Json

data class Edits(
    @field:Json(name = "object")
    val obj: String,
    val created: Long,
    val usage: Usage,
    val choices: List<EditChoice>
)
