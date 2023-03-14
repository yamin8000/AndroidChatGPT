package io.github.aryantech.androidchatgpt.content.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.aryantech.androidchatgpt.R
import io.github.aryantech.androidchatgpt.ui.composables.PersianText
import io.github.aryantech.androidchatgpt.ui.composables.ScaffoldWithTitle
import io.github.aryantech.androidchatgpt.util.DateTimeUtils.toIso
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryContent(
    onBackClick: () -> Unit,
    onItemClick: (Long) -> Unit
) {
    val state = rememberHistoryState()

    ScaffoldWithTitle(
        title = stringResource(id = R.string.history),
        onBackClick = onBackClick,
        content = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    items(state.history.value) {
                        HistoryItem(
                            title = it.title,
                            date = it.date,
                            onClick = { onItemClick(it.id) }
                        )
                    }
                }
            )
        }
    )
}

@Composable
fun HistoryItem(
    title: String,
    date: ZonedDateTime,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.clickable(onClick = onClick),
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                content = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PersianText(
                            text = stringResource(R.string.title),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        PersianText(title)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PersianText(
                            text = stringResource(R.string.date),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(date.toLocalDateTime().toIso())
                    }
                }
            )
        }
    )
}