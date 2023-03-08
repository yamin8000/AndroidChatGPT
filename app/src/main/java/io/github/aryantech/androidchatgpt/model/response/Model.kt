package io.github.aryantech.androidchatgpt.model.response

import com.squareup.moshi.Json

data class Model(
    val id: String,
    @field:Json(name = "object") val obj: String,
    val created: Long,
    @field:Json(name = "owned_by") val ownedBy: String,
    val permission: List<ModelPermission>
)
