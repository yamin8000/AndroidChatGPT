package io.github.rayangroup.hamyar.content.images

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.ImageSearch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
            if (state.imageUrls.value.size <= 1) 1
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
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
                            item {
                                if (state.imageUrls.value.isEmpty())
                                    EmptyList()
                            }
                            items(state.imageUrls.value) {
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillWidth,
                                    contentDescription = state.imagePrompt.value,
                                    model = it
                                )
                            }
                        }
                    )
                }
            }
        }
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