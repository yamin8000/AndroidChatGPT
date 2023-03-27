package io.github.rayan_group.hamyar.model.response

import com.squareup.moshi.Json

data class Models(
    @field:Json(name = "object") val obj: String,
    val data: List<Model>,
    val root: String,
    val parent: String?
)
