package io.github.aryantech.androidchatgpt.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.ArrowDropDownCircle
import androidx.compose.material.icons.twotone.DisplaySettings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
import io.github.aryantech.androidchatgpt.ui.theme.DefaultShape
import io.github.aryantech.androidchatgpt.ui.theme.IranYekan
import io.github.aryantech.androidchatgpt.util.Constants
import io.github.aryantech.androidchatgpt.util.Constants.DNS_SERVERS
import io.github.aryantech.androidchatgpt.util.Constants.INTERNET_CHECK_DELAY
import io.github.aryantech.androidchatgpt.util.getCurrentLocale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun InternetAwareComposable(
    dnsServers: List<String> = DNS_SERVERS,
    delay: Long = INTERNET_CHECK_DELAY,
    successContent: (@Composable () -> Unit)? = null,
    errorContent: (@Composable () -> Unit)? = null,
    onlineChanged: ((Boolean) -> Unit)? = null
) {
    suspend fun dnsAccessible(
        dnsServer: String
    ) = try {
        withContext(Dispatchers.IO) {
            Runtime.getRuntime().exec("/system/bin/ping -c 1 $dnsServer").waitFor()
        } == 0
    } catch (e: Exception) {
        false
    }

    var isOnline by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            isOnline = dnsServers.any { dnsAccessible(it) }
            onlineChanged?.invoke(isOnline)
            delay(delay)
        }
    }
    if (isOnline) successContent?.invoke()
    else errorContent?.invoke()
}

@Composable
fun MySnackbar(
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null,
    dismissAction: @Composable (() -> Unit)? = null,
    actionOnNewLine: Boolean = false,
    containerColor: Color = SnackbarDefaults.color,
    contentColor: Color = SnackbarDefaults.contentColor,
    actionContentColor: Color = SnackbarDefaults.actionContentColor,
    dismissActionContentColor: Color = SnackbarDefaults.dismissActionContentColor,
    content: @Composable () -> Unit
) {
    Snackbar(
        modifier = modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .padding(WindowInsets.ime.asPaddingValues()),
        action = action,
        dismissAction = dismissAction,
        actionOnNewLine = actionOnNewLine,
        shape = RoundedCornerShape(10.dp),
        containerColor = containerColor,
        contentColor = contentColor,
        actionContentColor = actionContentColor,
        dismissActionContentColor = dismissActionContentColor,
        content = content
    )
}

@Composable
fun <T> SettingChangerDialog(
    isEnabled: Boolean,
    title: String,
    options: List<T>,
    currentSetting: T,
    onSettingChange: (T) -> Unit,
    onDismiss: () -> Unit,
    displayProvider: ((T) -> String) = { it.toString() }
) {
    if (isEnabled) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = { /*ignored*/ },
            title = { PersianText(title) },
            icon = { Icon(imageVector = Icons.TwoTone.DisplaySettings, contentDescription = null) },
            text = {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(16.dp)
                        .selectableGroup()
                        .fillMaxWidth()
                ) {
                    items(options) { setting ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (setting == currentSetting),
                                    role = Role.RadioButton,
                                    onClick = {
                                        onSettingChange(setting)
                                        onDismiss()
                                    }
                                )
                        ) {
                            RadioButton(
                                selected = (setting == currentSetting),
                                onClick = null,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            PersianText(
                                text = displayProvider(setting),
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SettingsItemCard(
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        PersianText(
            text = title,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Card(
            modifier = modifier,
            shape = DefaultShape
        ) {
            Column(
                modifier = columnModifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = { content() }
            )
        }
    }
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = LocalIndication.current,
            onClick = onClick,
        ),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 16.dp),
                    content = content
                )
                Icon(
                    imageVector = Icons.TwoTone.ArrowDropDownCircle,
                    contentDescription = ""
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTitle(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    actions: @Composable RowScope.() -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHost,
        bottomBar = bottomBar,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Surface(
                shadowElevation = 8.dp,
                content = {
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
                            actions()
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
            )
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
    val currentLocale = getCurrentLocale(LocalContext.current).language
    var localFontWeight = fontWeight
    if (currentLocale == Locale("fa").language || Constants.PERSIAN_REGEX.containsMatchIn(text)
    ) {
        localFontFamily = IranYekan
        localStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Rtl)
        localFontWeight = FontWeight.ExtraBold
    }
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = localFontWeight,
        fontFamily = localFontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = localStyle
    )
}