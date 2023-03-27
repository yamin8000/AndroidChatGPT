package io.github.rayan_group.hamyar.model.response

import com.squareup.moshi.Json

data class Edits(
    @field:Json(name = "object")
    val obj: String,
    val created: Long,
    val usage: Usage,
    val choices: List<EditChoice>
)
