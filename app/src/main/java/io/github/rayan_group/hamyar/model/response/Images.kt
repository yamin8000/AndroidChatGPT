package io.github.rayan_group.hamyar.model.response

data class Images<T>(
    val create: Long,
    val data: List<T>
)
