package io.github.rayangroup.hamyar.content.about

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import io.github.rayangroup.hamyar.R
import io.github.rayangroup.hamyar.ui.composables.PersianText
import io.github.rayangroup.hamyar.ui.composables.ScaffoldWithTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutContent(
    onBackClick: () -> Unit
) {
    ScaffoldWithTitle(
        title = stringResource(R.string.about),
        onBackClick = onBackClick,
        content = {
            PersianText(
                textAlign = TextAlign.Justify,
                text = stringResource(id = R.string.about_text)
            )
        }
    )
}