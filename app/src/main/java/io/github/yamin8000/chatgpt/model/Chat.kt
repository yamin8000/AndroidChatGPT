package io.github.yamin8000.chatgpt.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val role: String,
    val content: String,
    val name: String? = null
) : Parcelable