package io.github.rayangroup.hamyar.content.images

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.BrokenImage
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.ImageSearch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import io.github.rayangroup.hamyar.R
import io.github.rayangroup.hamyar.ui.composables.PersianText
import io.github.rayangroup.hamyar.ui.composables.PersianTextField
import io.github.rayangroup.hamyar.ui.composables.ScaffoldWithTitle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ImagesContent(
    onBackClick: () -> Unit
) {
    val state = rememberImagesState()

    val gridSpan = remember {
        derivedStateOf {
            if ((state.imageUrls.value?.size ?: 0) <= 1) 1
            else 2
        }
    }
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
                        state.generateImages(state.imagePrompt.value)
                    }
                }
            )
        }, content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.isWaitingForResponse.value)
                    CircularProgressIndicator()
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(gridSpan.value),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    content = {
                        if (state.imageUrls.value != null) {
                            items(state.imageUrls.value ?: listOf()) {
                                SingleImage(state.imagePrompt.value, it)
                            }
                        } else item { EmptyList() }
                    }
                )
            }
        }
    )
}

@Composable
private fun SingleImage(
    description: String,
    url: String
) {
    var imagesState: AsyncImagePainter.State by remember { mutableStateOf(AsyncImagePainter.State.Empty) }
    when (imagesState) {
        is AsyncImagePainter.State.Loading -> {
            CircularProgressIndicator(modifier = Modifier.size(25.dp))
        }
        is AsyncImagePainter.State.Error -> {
            Image(
                modifier = Modifier.size(100.dp),
                imageVector = Icons.TwoTone.BrokenImage,
                contentDescription = ""
            )
        }
        else -> {}
    }
    AsyncImage(
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillWidth,
        contentDescription = description,
        model = url,
        onState = { imagesState = it }
    )
}

@Composable
fun EmptyList() {
    PersianText(stringResource(R.string.empty_list))
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