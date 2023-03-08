package io.github.aryantech.androidchatgpt.web

import io.github.aryantech.androidchatgpt.model.request.CreateCompletion
import io.github.aryantech.androidchatgpt.model.response.Completion
import io.github.aryantech.androidchatgpt.model.response.Model
import io.github.aryantech.androidchatgpt.model.response.Models
import retrofit2.http.GET
import retrofit2.http.POST

sealed interface AppAPIs

interface APIs {

    @GET("models")
    suspend fun getAllModels(): Models

    @GET("models/{model}")
    suspend fun getModel(model: String): Model

    @POST("completions")
    suspend fun createCompletion(createCompletion: CreateCompletion): Completion
}