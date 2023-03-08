package io.github.aryantech.androidchatgpt.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.aryantech.androidchatgpt.R
import io.github.aryantech.androidchatgpt.ui.theme.IranYekan
import io.github.aryantech.androidchatgpt.util.getCurrentLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTitle(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    onBackClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Surface(
                shadowElevation = 8.dp
            ) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        PersianText(
                            text = title,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = onBackClick,
                            content = {
                                Icon(
                                    imageVector = Icons.TwoTone.ArrowBack,
                                    contentDescription = stringResource(R.string.back)
                                )
                            }
                        )
                    }
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp)
                    .fillMaxHeight(),
                content = { content() }
            )
        }
    )
}

@Composable
fun PersianText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = 14.sp,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    var localStyle = style
    var localFontFamily = fontFamily
    val currentLocale = getCurrentLocale(LocalContext.current)
    if (currentLocale.language == Locale("fa").language) {
        localFontFamily = IranYekan
        localStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Rtl)
    }
    Text(
        text,
        modifier,
        color,
        fontSize,
        fontStyle,
        fontWeight,
        localFontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        onTextLayout,
        localStyle
    )
}