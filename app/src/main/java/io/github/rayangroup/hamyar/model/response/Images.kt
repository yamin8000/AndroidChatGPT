package io.github.rayangroup.hamyar.model.response

data class Images<T>(
    val create: Long,
    val data: List<T>
)
