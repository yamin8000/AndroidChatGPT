/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     ModelPermission.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     ModelPermission.kt Last modified at 2023/12/1
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

package io.github.yamin8000.chatgpt.model.response

import com.squareup.moshi.Json

data class ModelPermission(
    val id: String,
    @field:Json(name = "object") val obj: String,
    val created: Long,
    @field:Json(name = "allow_create_engine") val allowCreateEngine: Boolean,
    @field:Json(name = "allow_sampling") val allowSampling: Boolean,
    @field:Json(name = "allow_logprobs") val allowLogprobs: Boolean,
    @field:Json(name = "allow_search_indices") val allowSearchIndices: Boolean,
    @field:Json(name = "allow_view") val allowView: Boolean,
    @field:Json(name = "allow_fine_tuning") val allowFineTuning: Boolean,
    val organization: String,
    val group: String?,
    @field:Json(name = "is_blocking") val isBlocking: Boolean
)
