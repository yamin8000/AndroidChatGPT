package io.github.rayan_group.hamyar.content.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Menu
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rayan_group.hamyar.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsClick: () -> Unit,
    onNavigationClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val title = stringResource(R.string.app_name)
    Surface(
        shadowElevation = 8.dp
    ) {
        TopAppBar(
            scrollBehavior = scrollBehavior,
            title = { Text(title) },
            navigationIcon = {
                IconButton(
                    onClick = onNavigationClick,
                    content = {
                        Icon(
                            imageVector = Icons.TwoTone.Menu,
                            contentDescription = null
                        )
                    }
                )
            },
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
                IconButton(
                    onClick = onAboutClick,
                    content = {
                        Icon(
                            imageVector = Icons.TwoTone.Info,
                            contentDescription = stringResource(R.string.about)
                        )
                    }
                )
            }
        )
    }
}