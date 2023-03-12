package io.github.aryantech.androidchatgpt.content.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Send
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.aryantech.androidchatgpt.R
import io.github.aryantech.androidchatgpt.ui.animation.AnimatedDots
import io.github.aryantech.androidchatgpt.ui.composables.MySnackbar
import io.github.aryantech.androidchatgpt.ui.composables.PersianText
import io.github.aryantech.androidchatgpt.ui.composables.ScaffoldWithTitle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatContent(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val state = rememberChatState()

    val listState = rememberLazyListState()

    LaunchedEffect(state.chat.value.size) {
        val index = state.chat.value.size - 1
        if (index > 0)
            listState.animateScrollToItem(index)
    }

    ScaffoldWithTitle(
        title = stringResource(R.string.new_chat) + " ${state.model.value}",
        onBackClick = onBackClick,
        snackbarHost = {
            SnackbarHost(state.snackbarHost) { data ->
                MySnackbar {
                    PersianText(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = data.visuals.message
                    )
                }
            }
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
        },
        content = {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.chat.value) {
                    ChatBubble(
                        content = it.content,
                        owner = ChatBubbleOwner.of(it.role)
                    )
                }
                if (state.isWaitingForResponse) {
                    item {
                        ChatBubble(
                            owner = ChatBubbleOwner.Ai,
                            content = {
                                Box(
                                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                    content = { AnimatedDots() }
                                )
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar {
                TextField(
                    singleLine = false,
                    label = { PersianText(text = stringResource(R.string.chat_input)) },
                    value = state.chatInput.value,
                    onValueChange = { state.chatInput.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp, 16.dp, 0.dp)
                        .combinedClickable(
                            onClick = {},
                            onDoubleClick = { }
                        ),
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
                            enabled = state.chatInput.value.isNotBlank(),
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
                        imeAction = if (state.chatInput.value.isNotBlank()) ImeAction.Send else ImeAction.None,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(onSend = { state.newChatInput(state.chatInput.value) }),
                )
            }
        }
    )
}

@Composable
fun ChatBubble(
    content: String,
    owner: ChatBubbleOwner,
) {
    var isExpanded by remember { mutableStateOf(false) }

    ChatBubble(
        owner = owner,
        content = {
            PersianText(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { isExpanded = !isExpanded },
                text = content.trim(),
                overflow = TextOverflow.Ellipsis,
                maxLines = if (isExpanded) Int.MAX_VALUE else 10
            )
        }
    )
}

@Composable
fun ChatBubble(
    content: @Composable (ColumnScope.() -> Unit),
    owner: ChatBubbleOwner
) {
    val container = if (owner == ChatBubbleOwner.User) {
        MaterialTheme.colorScheme.secondaryContainer
    } else MaterialTheme.colorScheme.tertiaryContainer

    val shape = if (owner == ChatBubbleOwner.User) {
        RoundedCornerShape(
            topStart = 2.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomEnd = 2.dp,
            bottomStart = 16.dp
        )
    }

    val arrangement = if (owner == ChatBubbleOwner.User) Arrangement.Start else Arrangement.End

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement
    ) {
        ElevatedCard(
            modifier = Modifier.padding(4.dp),
            colors = CardDefaults.cardColors(containerColor = container),
            shape = shape,
            content = content
        )
    }
}