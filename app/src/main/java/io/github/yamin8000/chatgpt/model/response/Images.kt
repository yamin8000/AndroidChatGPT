package io.github.yamin8000.chatgpt.model.response

data class Images<T>(
    val create: Long,
    val data: List<T>
)
