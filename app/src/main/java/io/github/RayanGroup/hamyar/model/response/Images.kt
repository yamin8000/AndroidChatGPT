package io.github.RayanGroup.hamyar.model.response

data class Images<T>(
    val create: Long,
    val data: List<T>
)
