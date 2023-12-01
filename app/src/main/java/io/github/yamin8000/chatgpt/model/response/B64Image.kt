package io.github.yamin8000.chatgpt.model.response

import com.squareup.moshi.Json

@JvmInline
value class B64Image(
    @field:Json(name = "b64_json")
    val b64Json: String
)
