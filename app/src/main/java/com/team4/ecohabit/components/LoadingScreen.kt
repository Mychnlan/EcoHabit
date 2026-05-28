package com.team4.ecohabit.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.ui.theme.backgroundCardLight
import com.team4.ecohabit.ui.theme.brightGreen

@Composable
fun PageLoading() {

    val transition = rememberInfiniteTransition(
        label = ""
    )

    val scale by transition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,

        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 900,
                easing = FastOutSlowInEasing
            ),

            repeatMode = RepeatMode.Reverse
        ),

        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundCardLight),

        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            modifier = Modifier
                .size(52.dp)
                .scale(scale),

            color = brightGreen,
            strokeWidth = 5.dp
        )
    }
}