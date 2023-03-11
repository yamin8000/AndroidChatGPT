package io.github.aryantech.androidchatgpt.content.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.aryantech.androidchatgpt.R
import io.github.aryantech.androidchatgpt.content.home.MainTopAppBar
import io.github.aryantech.androidchatgpt.ui.composables.PersianText
import io.github.aryantech.androidchatgpt.ui.composables.ScaffoldWithTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatContent(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val state = rememberChatState()

    val listState = rememberLazyListState()

    LaunchedEffect(state.chat.value.size) {
        val index = state.chat.value.size - 1
        if (index > 0)
            listState.animateScrollToItem(index)
    }

    ScaffoldWithTitle(
        title = stringResource(R.string.new_chat),
        onBackClick = onBackClick,
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
                item {
                    TextField(
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth(),
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
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        keyboardActions = KeyboardActions(onSend = { state.newChatInput(state.chatInput.value) }),
                    )
                }
            }
        }
    )
}

@Composable
fun ChatBubble(
    content: String,
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
            shape = shape
        ) {
            PersianText(
                modifier = Modifier.padding(8.dp),
                text = content
            )
        }

    }

}