package io.github.rayangroup.hamyar.model.response

import com.squareup.moshi.Json

@JvmInline
value class B64Image(
    @field:Json(name = "b64_json")
    val b64Json: String
)