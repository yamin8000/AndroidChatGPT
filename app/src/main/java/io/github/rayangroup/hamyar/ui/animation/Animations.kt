package io.github.rayangroup.hamyar.ui.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

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