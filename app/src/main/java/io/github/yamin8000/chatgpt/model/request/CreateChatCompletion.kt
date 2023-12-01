/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     CreateChatCompletion.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     CreateChatCompletion.kt Last modified at 2023/12/1
 *     This file is part of AndroidChatGPT/AndroidChatGPT.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     AndroidChatGPT/AndroidChatGPT.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     AndroidChatGPT/AndroidChatGPT.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with AndroidChatGPT.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.chatgpt.model.request

import com.squareup.moshi.Json
import io.github.yamin8000.chatgpt.model.Chat

data class CreateChatCompletion(
    val model: String,
    val messages: List<Chat>,
    val temperature: Int? = 1,
    @field:Json(name = "top_p")
    val topP: Int? = 1,
    val n: Int? = 1,
    val stream: Boolean? = false,
    val stop: List<String>? = null,
    @field:Json(name = "max_tokens")
    val maxTokens: Int? = 100,
    @field:Json(name = "presence_penalty")
    val presencePenalty: Float? = 0f,
    @field:Json(name = "frequency_penalty")
    val frequencyPenalty: Float? = 0f,
    @Suppress("SpellCheckingInspection")
    @field:Json(name = "logit_bias")
    val logitBias: Map<String, Int>? = null,
    val user: String? = null
)
