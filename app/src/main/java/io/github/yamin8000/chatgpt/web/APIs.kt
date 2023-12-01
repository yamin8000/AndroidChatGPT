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