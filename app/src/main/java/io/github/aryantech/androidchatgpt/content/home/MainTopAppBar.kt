package io.github.aryantech.androidchatgpt.content.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.aryantech.androidchatgpt.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsClick: () -> Unit
) {
    val title = stringResource(R.string.app_name)
    Surface(
        shadowElevation = 8.dp
    ) {
        TopAppBar(
            scrollBehavior = scrollBehavior,
            title = { Text(title) },
            actions = {
                IconButton(
                    onClick = onSettingsClick,
                    content = {
                        Icon(
                            imageVector = Icons.TwoTone.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                )
            }
        )
    }
}