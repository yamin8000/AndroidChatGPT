/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     CreateCompletion.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     CreateCompletion.kt Last modified at 2023/12/1
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

data class CreateCompletion(
    val model: String,
    val prompt: List<String>?,
    val suffix: String? = null,
    @field:Json(name = "max_tokens")
    val maxTokens: Int? = 16,
    val temperature: Float? = 1f,
    @field:Json(name = "top_p")
    val topP: Float? = 1f,
    val n: Int? = 1,
    val stream: Boolean? = false,
    val logprobs: Int? = null,
    val echo: Boolean? = false,
    val stop: List<String>? = null,
    @field:Json(name = "presence_penalty")
    val presencePenalty: Float? = 0f,
    @field:Json(name = "frequency_penalty")
    val frequencyPenalty: Float? = 0f,
    @field:Json(name = "best_of") val bestOf: Int? = 1,
    @Suppress("SpellCheckingInspection")
    @field:Json(name = "logit_bias")
    val logitBias: Map<String, Int>? = null,
    val user: String? = null
)
