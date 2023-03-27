package io.github.aryantech.androidchatgpt.content.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImagePainter
import io.github.aryantech.androidchatgpt.model.request.CreateUrlImage
import io.github.aryantech.androidchatgpt.util.log
import io.github.aryantech.androidchatgpt.web.APIs
import io.github.aryantech.androidchatgpt.web.Web
import io.github.aryantech.androidchatgpt.web.Web.apiOf
import retrofit2.HttpException

class ImagesState(
    val imagePrompt: MutableState<String>,
    val imageUrl: MutableState<String>,
    val imageState: MutableState<AsyncImagePainter.State>,
    val isWaitingForResponse: MutableState<Boolean>,
    val scope: LifecycleCoroutineScope
) {
    suspend fun generateImage(
        newPrompt: String
    ) {
        imageUrl.value = try {
            isWaitingForResponse.value = true
            Web.getRetrofit()
                .apiOf<APIs.ImagesAPIs>()
                .createUrlImage(createImage = CreateUrlImage(prompt = newPrompt))
                .data
                .first()
                .url
        } catch (e: Exception) {
            e.stackTraceToString().log()
            (e as? HttpException)?.response()?.errorBody()?.string()?.log()
            ""
        } finally {
            imageUrl.value.log()
            isWaitingForResponse.value = false
        }
    }
}

@Composable
fun rememberImagesState(
    imagePrompt: MutableState<String> = rememberSaveable { mutableStateOf("") },
    imageUrl: MutableState<String> = rememberSaveable { mutableStateOf("") },
    imageState: MutableState<AsyncImagePainter.State> = remember { mutableStateOf(AsyncImagePainter.State.Empty) },
    isWaitingForResponse: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    scope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope
) = remember(imagePrompt, imageUrl, imageState, isWaitingForResponse, scope) {
    ImagesState(imagePrompt, imageUrl, imageState, isWaitingForResponse, scope)
}