package io.github.rayangroup.hamyar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val role: String,
    val content: String
) : Parcelable
