package io.github.aryantech.androidchatgpt.web

import io.github.aryantech.androidchatgpt.model.response.ChatCompletion
import io.github.aryantech.androidchatgpt.model.request.CreateChatCompletion
import io.github.aryantech.androidchatgpt.model.request.CreateCompletion
import io.github.aryantech.androidchatgpt.model.response.Completion
import io.github.aryantech.androidchatgpt.model.response.Model
import io.github.aryantech.androidchatgpt.model.response.Models
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

sealed interface AppAPIs

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
}