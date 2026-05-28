package com.team4.ecohabit.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp


@Composable
fun HabitCircularProgress(
    modifier: Modifier = Modifier,
    progress: Float
) {

    val percentage = (progress * 100).toInt()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            drawArc(
                color = Color(
                    0xFFEAEAEA
                ),

                startAngle = -90f,

                sweepAngle = 360f,

                useCenter = false,

                style = Stroke(
                    width = 28f,
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                brush = Brush.sweepGradient(
                    listOf(
                        Color(0xFF9BE15D),
                        Color(0xFF00E3AE),
                        Color(0xFF00C853)
                    )
                ),

                startAngle = -90f,

                sweepAngle =
                    360 * progress,

                useCenter = false,

                style = Stroke(
                    width = 28f,
                    cap = StrokeCap.Round
                )
            )
        }

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = "$percentage%",
                style =
                    MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Done",
                style =
                    MaterialTheme.typography.bodySmall
            )
        }
    }
}