package io.github.RayanGroup.hamyar.model.response

import com.squareup.moshi.Json

data class ModelPermission(
    val id: String,
    @field:Json(name = "object") val obj: String,
    val created: Long,
    @field:Json(name = "allow_create_engine") val allowCreateEngine: Boolean,
    @field:Json(name = "allow_sampling") val allowSampling: Boolean,
    @field:Json(name = "allow_logprobs") val allowLogprobs: Boolean,
    @field:Json(name = "allow_search_indices") val allowSearchIndices: Boolean,
    @field:Json(name = "allow_view") val allowView: Boolean,
    @field:Json(name = "allow_fine_tuning") val allowFineTuning: Boolean,
    val organization: String,
    val group: String?,
    @field:Json(name = "is_blocking") val isBlocking: Boolean
)
