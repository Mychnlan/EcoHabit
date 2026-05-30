package com.team4.ecohabit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team4.ecohabit.R
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.textColor

@Composable
fun DailyTargetCard() {

    var target by remember {
        mutableIntStateOf(1)
    }

    EcoCard {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.ic_target),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = "Daily Target",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = brightGreen
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    if (target > 1) target--
                }
            ) {
                Text(
                    "-",
                    fontSize = 24.sp
                )
            }

            Text(
                text = target.toString(),
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            IconButton(
                onClick = {
                    target++
                }
            ) {
                Text(
                    "+",
                    fontSize = 24.sp
                )
            }

            Spacer(Modifier.width(16.dp))

            Text(
                text = "times per day",
                color = textColor
            )
        }
    }
}