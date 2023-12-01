/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     APIs.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     APIs.kt Last modified at 2023/12/1
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

package io.github.yamin8000.chatgpt.web

import io.github.yamin8000.chatgpt.model.request.CreateChatCompletion
import io.github.yamin8000.chatgpt.model.request.CreateCompletion
import io.github.yamin8000.chatgpt.model.request.CreateEdit
import io.github.yamin8000.chatgpt.model.request.CreateUrlImage
import io.github.yamin8000.chatgpt.model.response.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

@Suppress("unused")
object APIs {

    interface ModelsAPIs {
        @GET("models")
        suspend fun getAllModels(): Models

        @GET("models/{model}")
        suspend fun getModel(model: String): Model
    }

    interface CompletionsAPIs {
        @POST("completions")
        suspend fun createCompletion(@Body createCompletion: CreateCompletion): Completion
    }

    interface ChatCompletionsAPIs {

        @POST("chat/completions")
        suspend fun createChatCompletions(@Body createChatCompletion: CreateChatCompletion): ChatCompletion
    }

    interface EditsAPIs {

        @POST("edits")
        suspend fun createEdit(@Body createEdit: CreateEdit): Edits
    }

    interface ImagesAPIs {
        @POST("images/generations")
        suspend fun createUrlImage(@Body createImage: CreateUrlImage): Images<UrlImage>

        @POST("images/generations")
        suspend fun createB64Image(@Body createImage: CreateUrlImage): Images<B64Image>
    }
}