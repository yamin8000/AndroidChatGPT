/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     CreateB64Image.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     CreateB64Image.kt Last modified at 2023/12/1
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

/**
 * @param n The number of images to generate. Must be between 1 and 10.
 * @param size The size of the generated images. Must be one of 256x256, 512x512, or 1024x1024.
 * @param responseFormat The format in which the generated images are returned. Must be one of url or b64_json.
 */
data class CreateB64Image(
    val prompt: String,
    val n: Int = 1,
    val size: String = "256x256",
    @field:Json(name = "response_format")
    val responseFormat: String = "b64_json",
    val user: String?
)

