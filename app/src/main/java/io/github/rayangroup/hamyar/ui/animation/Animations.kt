package io.github.rayangroup.hamyar.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.rayangroup.hamyar.ui.composables.PersianText
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
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
    style: TextStyle = LocalTextStyle.current,
    overrideDirection: Boolean = true,
    forcePersian: Boolean = false,
    typewriterDelay: Long = 50L,
    typeWriterPass: () -> Unit
) {
    val builder = StringBuilder()
    val chars = remember { text.toCharArray() }
    var currentText by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        chars.forEachIndexed { index, char ->
            builder.append(char)
            currentText = builder.toString()
            delay(typewriterDelay)
            if (index % 15 == 0)
                typeWriterPass()
        }
    }
    PersianText(
        text = AnnotatedString(currentText),
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = style,
        overrideDirection = overrideDirection,
        forcePersian = forcePersian
    )
}

@Composable
fun AnimatedDots() {
    val dots = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )

    dots.forEachIndexed { index, item ->
        LaunchedEffect(item) {
            delay(index * 100L)
            item.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    repeatMode = RepeatMode.Restart,
                    animation = keyframes {
                        durationMillis = 1000
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 200 with LinearOutSlowInEasing
                        0.0f at 400 with LinearOutSlowInEasing
                        0.0f at 1000
                    },
                )
            )
        }
    }

    val dys = dots.map { it.value }
    val travelDistance = with(LocalDensity.current) { 2.dp.toPx() }

    Box(
        modifier = Modifier.height(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            dys.forEach { dy ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(y = (-dy * travelDistance).dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onTertiaryContainer)
                )
            }
        }
    }
}