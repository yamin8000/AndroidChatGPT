package io.github.aryantech.androidchatgpt.content.chat

enum class ChatBubbleOwner {
    User, Ai;

    companion object {
        fun of(value: String) = if (value == "user") User else Ai
    }
}