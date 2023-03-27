package io.github.rayan_group.hamyar.content.about

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.rayan_group.hamyar.R
import io.github.rayan_group.hamyar.ui.composables.ScaffoldWithTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutContent(
    onBackClick: () -> Unit
) {
    ScaffoldWithTitle(
        title = stringResource(R.string.about),
        onBackClick = onBackClick,
        content = {

        }
    )
}