/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     CreateEdit.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     CreateEdit.kt Last modified at 2023/12/1
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
import io.github.yamin8000.chatgpt.util.Constants

data class CreateEdit(
    val instruction: String,
    val n: Int = 1,
    val temperature: Float = 1f,
    @field:Json(name = "top_p")
    val topP: Float? = 1f,
    val model: String = Constants.EDIT_MODELS.first(),
    val input: String = "",
)
