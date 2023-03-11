package io.github.aryantech.androidchatgpt.content.home

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
import io.github.aryantech.androidchatgpt.content.chat.ChatBubble
import io.github.aryantech.androidchatgpt.content.chat.ChatBubbleOwner
import io.github.aryantech.androidchatgpt.ui.composables.PersianText
import io.github.aryantech.androidchatgpt.ui.composables.ScaffoldWithTitle
import okhttp3.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    onSettingsClick: () -> Unit,
    onNewChat: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MainTopAppBar(
                scrollBehavior = scrollBehavior,
                onSettingsClick = onSettingsClick
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Column {
                    Button(
                        onClick = onNewChat,
                        content = { PersianText(stringResource(R.string.new_chat)) }
                    )
                }
            }
        }
    )
}