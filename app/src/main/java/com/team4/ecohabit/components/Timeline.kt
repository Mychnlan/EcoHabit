package com.team4.ecohabit.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.model.HistoryItem
import com.team4.ecohabit.ui.theme.backgroundCardLight
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.buttonAction
import com.team4.ecohabit.ui.theme.greenLogo
import com.team4.ecohabit.ui.theme.textColor
import com.team4.ecohabit.ui.theme.titleTextColor
import com.team4.ecohabit.ui.theme.white
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun HistoryTimeline(
    histories: List<HistoryItem>
) {

    if (histories.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "No history yet 🌱",
                color = textColor
            )
        }

        return
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        histories.forEach { history ->

            Row {

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(
                                greenLogo,
                                CircleShape
                            )
                    )

                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(120.dp)
                            .background(
                                backgroundCardLight
                            )
                    )
                }

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = formatDate(
                            history.completedAt
                        ),

                        style = MaterialTheme.typography.titleLarge,

                        color = greenLogo
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    HistoryCard(
                        history = history
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryCard(
    history: HistoryItem
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = white
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        buttonAction,
                        CircleShape
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    painter = painterResource(
                        history.icon
                    ),

                    contentDescription = null,

                    tint = greenLogo
                )
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = history.habitName,

                    style =
                        MaterialTheme.typography.titleLarge,

                    color = titleTextColor
                )

                Text(
                    text = formatTime(
                        history.completedAt
                    ),

                    style =
                        MaterialTheme.typography.bodyMedium,

                    color = textColor
                )
            }

            Text(
                text = "+${history.points}",

                color = brightGreen,

                style =
                    MaterialTheme.typography.titleLarge
            )
        }
    }
}

private fun formatTime(
    timestamp: Long
): String {

    return SimpleDateFormat(
        "hh:mm a",
        Locale.getDefault()
    ).format(
        Date(timestamp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(
    timestamp: Long
): String {

    val date =
        Instant.ofEpochMilli(timestamp)
            .atZone(
                ZoneId.systemDefault()
            )
            .toLocalDate()

    val today =
        LocalDate.now()

    return when (date) {

        today -> "Today"

        today.minusDays(1) ->
            "Yesterday"

        else ->
            date.format(
                DateTimeFormatter.ofPattern(
                    "dd MMM yyyy"
                )
            )
    }
}