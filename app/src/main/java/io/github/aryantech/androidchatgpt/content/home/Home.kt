package io.github.aryantech.androidchatgpt.content.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.aryantech.androidchatgpt.R
import io.github.aryantech.androidchatgpt.ui.composables.PersianText
import okhttp3.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    onSettingsClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val state = rememberHomeState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MainTopAppBar(
                    scrollBehavior = scrollBehavior,
                    onSettingsClick = onSettingsClick
                )
            },
            bottomBar = {
                BottomAppBar {
                    TextField(
                        singleLine = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 16.dp, 0.dp),
                        label = {
                            PersianText(
                                text = stringResource(R.string.chat_input)
                            )
                        },
                        value = state.chatInput.value,
                        onValueChange = { state.chatInput.value = it },
                        leadingIcon = {
                            IconButton(
                                onClick = { state.chatInput.value = "" },
                                content = {
                                    Icon(
                                        imageVector = Icons.TwoTone.Clear,
                                        contentDescription = stringResource(R.string.delete)
                                    )
                                }
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { state.newChatInput(state.chatInput.value) },
                                content = {
                                    Icon(
                                        imageVector = Icons.TwoTone.Send,
                                        contentDescription = stringResource(R.string.send)
                                    )
                                }
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send,
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        keyboardActions = KeyboardActions(onSend = { state.newChatInput(state.chatInput.value) }),
                    )
                }
            },
            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(8.dp)
                ) {
                    items(state.chat.value) {
                        Text(it)
                    }
                }
            }
        )
    }
}