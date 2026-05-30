package com.team4.ecohabit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team4.ecohabit.R
import com.team4.ecohabit.model.RepeatDay
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.buttonAction
import com.team4.ecohabit.ui.theme.textColor
import com.team4.ecohabit.ui.theme.white

@Composable
fun RepeatCard(
    selectedDays: Set<RepeatDay>,
    onDayClick: (RepeatDay) -> Unit
) {

    EcoCard {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.ic_repeat),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = "Repeat",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = brightGreen
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            RepeatDay.entries.forEach { day ->

                val isSelected = day in selectedDays

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) brightGreen
                            else buttonAction
                        )
                        .clickable {
                            onDayClick(day)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.shortName,
                        color = if (isSelected) white else textColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}