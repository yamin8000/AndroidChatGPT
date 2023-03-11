package io.github.aryantech.androidchatgpt.content.home

enum class ChatBubbleOwner {
    User, Ai;

    companion object {
        fun of(value: String) = if (value == "user") User else Ai
    }
}