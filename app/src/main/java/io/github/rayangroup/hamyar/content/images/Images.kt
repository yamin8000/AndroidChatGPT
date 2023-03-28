package io.github.rayangroup.hamyar.content.images

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.ImageSearch
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import io.github.rayangroup.hamyar.ui.composables.ScaffoldWithTitle
import kotlinx.coroutines.launch
import io.github.rayangroup.hamyar.R
import io.github.rayangroup.hamyar.ui.composables.PersianText
import io.github.rayangroup.hamyar.ui.composables.PersianTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesContent(
    onBackClick: () -> Unit
) {
    val state = rememberImagesState()

    ScaffoldWithTitle(
        title = stringResource(R.string.image_generation),
        onBackClick = onBackClick,
        bottomBar = {
            UserInput(
                isEnabled = !state.isWaitingForResponse.value,
                value = state.imagePrompt.value,
                onValueChanged = { state.imagePrompt.value = it },
                onSearch = {
                    state.scope.launch {
                        state.generateImage(state.imagePrompt.value)
                    }
                }
            )
        }, content = {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth,
                contentDescription = state.imageUrl.value,
                model = state.imageUrl.value,
                onState = { state.imageState.value = it }
            )
            when (state.imageState.value) {
                AsyncImagePainter.State.Empty -> {}
                is AsyncImagePainter.State.Error -> {}
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }
                is AsyncImagePainter.State.Success -> {}
            }
        }
    )
}

@Composable
fun UserInput(
    isEnabled: Boolean,
    value: String,
    onValueChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    PersianTextField(
        modifier = Modifier.fillMaxWidth(),
        enabled = isEnabled,
        maxLines = 2,
        value = value,
        onValueChange = onValueChanged,
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        keyboardOptions = KeyboardOptions(
            imeAction = if (value.isNotBlank()) ImeAction.Search else ImeAction.None,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences
        ),
        leadingIcon = {
            IconButton(
                onClick = { onValueChanged("") },
                content = {
                    Icon(
                        imageVector = Icons.TwoTone.Clear,
                        contentDescription = stringResource(R.string.clear)
                    )
                }
            )
        },
        trailingIcon = {
            IconButton(
                enabled = value.isNotBlank(),
                onClick = onSearch,
                content = {
                    Icon(
                        imageVector = Icons.TwoTone.ImageSearch,
                        contentDescription = stringResource(R.string.image_generation)
                    )
                }
            )
        },
        placeholder = {
            PersianText(text = stringResource(id = R.string.image_input_placeholder))
        }
    )

}