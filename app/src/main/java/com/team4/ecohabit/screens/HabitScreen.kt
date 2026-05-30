package com.team4.ecohabit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.R
import com.team4.ecohabit.components.CheckboxItemHabit
import com.team4.ecohabit.components.HabitCircularProgress
import com.team4.ecohabit.model.HabitItem
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.greenLogo
import com.team4.ecohabit.ui.theme.softGreen
import com.team4.ecohabit.ui.theme.textColor
import com.team4.ecohabit.ui.theme.white

@Composable
fun HabitScreen(
    onAddHabitClick: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),

            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 120.dp
            ),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item { HeaderSection() }

            item { ProgressSection() }

            item {
                DailyHabitSection()
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        ExtendedFloatingActionButton(
            onClick = onAddHabitClick,

            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = 16.dp
                ),

            contentColor = white,
            containerColor = brightGreen,

            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = null
                )
            },

            text = {
                Text("Add Habit")
            }
        )
    }
}

@Composable
private fun HeaderSection(){
    Text(
        text = "Today Habits",
        style = MaterialTheme.typography.headlineLarge,
        color = textColor
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = "Your small actions create big impacts. Let's make today greener.",
        style = MaterialTheme.typography.bodyMedium,
        color = textColor
    )
}

@Composable
private fun ProgressSection() {

    val completed = 5
    val total = 7

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),

        shape = RoundedCornerShape(
            25.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor = white
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(20.dp)
                .fillMaxWidth()
        ) {

            Column {

                Text(
                    text = "Daily Progress",
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor
                )

                Text(
                    text = "$completed / $total Completed",
                    style = MaterialTheme.typography.titleLarge,
                    color = greenLogo
                )
            }

            Spacer(
                modifier = Modifier.width(20.dp)
            )

            HabitCircularProgress(
                modifier = Modifier.size(65.dp),
                progress = completed / total.toFloat()
            )
        }
    }
}

@Composable
private fun DailyHabitSection(){
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Daily Habits",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val habits = listOf(
                HabitItem(
                    id = 1,
                    title = "Turn off lights",
                    isChecked = true,
                    R.drawable.ic_lamp
                ),
                HabitItem(
                    id = 2,
                    title = "Drink water",
                    isChecked = false,
                    R.drawable.ic_lamp
                ),
                HabitItem(
                    id = 3,
                    title = "Morning exercise",
                    isChecked = false,
                    R.drawable.ic_lamp
                )
            )

            CheckboxItemHabit(
                habits = habits
            )
        }
    }
}