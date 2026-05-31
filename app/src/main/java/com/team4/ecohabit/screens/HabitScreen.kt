package com.team4.ecohabit.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.team4.ecohabit.R
import com.team4.ecohabit.components.CheckboxItemHabit
import com.team4.ecohabit.components.HabitCircularProgress
import com.team4.ecohabit.model.Habit
import com.team4.ecohabit.model.HabitItem
import com.team4.ecohabit.model.HabitRepository
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.greenLogo
import com.team4.ecohabit.ui.theme.softGreen
import com.team4.ecohabit.ui.theme.textColor
import com.team4.ecohabit.ui.theme.white
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitScreen(
    onAddHabitClick: () -> Unit
) {

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var habits by remember {
        mutableStateOf<List<Habit>>(emptyList())
    }

    LaunchedEffect(userId) {

        if (userId == null) return@LaunchedEffect

        HabitRepository.getHabits(
            userId = userId,
            onResult = {
                habits = it
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),

            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 10.dp
            ),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                HeaderSection()
            }

            item {
                ProgressSection(
                    habits = habits
                )
            }

            item {

                DailyHabitSection(
                    habits = habits,
                    userId = userId
                )

                Spacer(
                    modifier = Modifier.height(60.dp)
                )
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ProgressSection(
    habits: List<Habit>
) {

    val total = habits.size

    val today =
        LocalDate.now().toString()

    val completed =
        habits.count {
            today in it.completedDates
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = white
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            horizontalArrangement = Arrangement.SpaceBetween,

            verticalAlignment = Alignment.CenterVertically
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

            HabitCircularProgress(
                modifier = Modifier.size(65.dp),

                progress =
                    if (total == 0)
                        0f
                    else
                        completed.toFloat() / total
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DailyHabitSection(
    habits: List<Habit>,
    userId: String?
) {

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

        if (habits.isEmpty()) {

            Text(
                text = "No habits yet 🌱"
            )

        } else {

            CheckboxItemHabit(
                habits = habits,

                onCheckedChange = { habit, checked ->

                    if (userId == null) return@CheckboxItemHabit

                    val today =
                        LocalDate.now().toString()

                    val updatedDates =

                        if (checked) {

                            (habit.completedDates + today)
                                .distinct()

                        } else {

                            habit.completedDates
                                .filterNot { it == today }
                        }

                    HabitRepository.updateHabitToday(
                        userId = userId,
                        habitId = habit.id,
                        completedDates = updatedDates
                    )
                }
            )
        }
    }
}