package io.github.aryantech.androidchatgpt.content.chat

enum class ChatBubbleOwner {
    User, Assistant;

    companion object {
        fun of(value: String) = if (value.lowercase() == "user") User else Assistant
    }
}