package io.github.yamin8000.chatgpt.model.request

import com.squareup.moshi.Json

/**
 * @param n The number of images to generate. Must be between 1 and 10.
 * @param size The size of the generated images. Must be one of 256x256, 512x512, or 1024x1024.
 * @param responseFormat The format in which the generated images are returned. Must be one of url or b64_json.
 */
data class CreateUrlImage(
    val prompt: String,
    val n: Int = 1,
    val size: String = "256x256",
    @field:Json(name = "response_format")
    val responseFormat: String = "url",
    val user: String? = null
)
