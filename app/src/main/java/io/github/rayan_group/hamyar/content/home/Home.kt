package io.github.rayan_group.hamyar.content.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rayan_group.hamyar.R
import io.github.rayan_group.hamyar.ui.composables.Lottie
import io.github.rayan_group.hamyar.ui.composables.PersianText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    onNavigateTo: (NavigationItem) -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    var selectedItem by remember { mutableStateOf(NavigationItem.Home) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                NavigationItem.values().forEach { item ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(
                            top = 0.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        ),
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = stringResource(item.labelId)
                            )
                        },
                        label = { PersianText(stringResource(item.labelId)) },
                        selected = selectedItem == item,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem = item
                            onNavigateTo(item)
                        }
                    )
                }
            }
        },
        content = {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    MainTopAppBar(
                        scrollBehavior = scrollBehavior,
                        onSettingsClick = { onNavigateTo(NavigationItem.Settings) },
                        onNavigationClick = { scope.launch { drawerState.open() } },
                        onAboutClick = { onNavigateTo(NavigationItem.About) }
                    )
                },
                content = { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(16.dp),
                        content = {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(
                                    content = { PersianText(stringResource(R.string.chat_with_me)) },
                                    onClick = { onNavigateTo(NavigationItem.NewChat) }
                                )
                                Lottie(R.raw.robot)
                            }
                        }
                    )
                }
            )
        }
    )
}