package io.github.aryantech.androidchatgpt.model.response

data class Images<T>(
    val create: Long,
    val data: List<T>
)
