package io.github.yamin8000.chatgpt.content.chat

enum class ChatBubbleOwner {
    User, Assistant;

    companion object {
        fun of(value: String) = if (value.lowercase() == "user") User else Assistant
    }
}