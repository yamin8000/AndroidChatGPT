package io.github.aryantech.androidchatgpt.web

import io.github.aryantech.androidchatgpt.model.Models
import retrofit2.http.GET

sealed interface AppAPIs

interface APIs {

    @GET("models")
    suspend fun getAllModels(): Models
}