/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     ImagesState.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     ImagesState.kt Last modified at 2023/12/1
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

package io.github.yamin8000.chatgpt.content.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.chatgpt.model.request.CreateUrlImage
import io.github.yamin8000.chatgpt.util.log
import io.github.yamin8000.chatgpt.web.APIs
import io.github.yamin8000.chatgpt.web.Web
import io.github.yamin8000.chatgpt.web.Web.apiOf
import retrofit2.HttpException

class ImagesState(
    val imagePrompt: MutableState<String>,
    val imageUrls: MutableState<List<String>?>,
    val isWaitingForResponse: MutableState<Boolean>,
    val scope: LifecycleCoroutineScope
) {
    suspend fun generateImages(
        newPrompt: String
    ) {
        imageUrls.value = try {
            isWaitingForResponse.value = true
            Web.getRetrofit()
                .apiOf<APIs.ImagesAPIs>()
                .createUrlImage(createImage = CreateUrlImage(prompt = newPrompt, n = 2))
                .data
                .map { it.url }
        } catch (e: Exception) {
            val errorBody = (e as? HttpException)?.response()?.errorBody()
            log(e)
            log(errorBody?.string() ?: "Unknown generate image error")
            null
        } finally {
            isWaitingForResponse.value = false
        }
    }
}

@Composable
fun rememberImagesState(
    imagePrompt: MutableState<String> = rememberSaveable { mutableStateOf("") },
    imageUrls: MutableState<List<String>?> = rememberSaveable { mutableStateOf(listOf()) },
    isWaitingForResponse: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    scope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope
) = remember(imagePrompt, imageUrls, isWaitingForResponse, scope) {
    ImagesState(imagePrompt, imageUrls, isWaitingForResponse, scope)
}