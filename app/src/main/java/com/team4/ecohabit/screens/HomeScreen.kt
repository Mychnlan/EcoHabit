package com.team4.ecohabit.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.team4.ecohabit.R
import com.team4.ecohabit.components.HabitCircularProgress
import com.team4.ecohabit.model.Habit
import com.team4.ecohabit.model.HabitRepository
import com.team4.ecohabit.ui.theme.activeIcon
import com.team4.ecohabit.ui.theme.activeMenu
import com.team4.ecohabit.ui.theme.backgroundCardLight
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.textColor
import com.team4.ecohabit.ui.theme.titleTextColor
import com.team4.ecohabit.ui.theme.white
import java.time.LocalDate

@SuppressLint("NewApi")
@Composable
fun HomeScreen(onAddHabitClick: () -> Unit) {
    val userId =
        FirebaseAuth.getInstance()
            .currentUser
            ?.uid

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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),

        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 16.dp
        ),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            ReminderSection()
        }

        item {
            ProgressSection(
                habits = habits
            )
        }

        item {
            QuickActionSection(
                onAddHabitClick = onAddHabitClick
            )
        }
    }
}


@Composable
private fun ReminderSection() {

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ReminderCard()
    }
}

@Composable
private fun QuickActionSection(
    onAddHabitClick: () -> Unit
) {

    Spacer(
        modifier = Modifier.height(20.dp)
    )

    Text(
        text = "Quick Action",
        style = MaterialTheme.typography.titleLarge,
        color = titleTextColor
    )

    Spacer(
        modifier = Modifier.height(12.dp)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        QuickActionButton(
            modifier = Modifier.weight(1f),
            icon = R.drawable.ic_plus,
            label = "Add Habit",
            onClick = onAddHabitClick
        )
    }
}

@Composable
private fun QuickActionButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    label: String,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = modifier.height(100.dp),

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = activeMenu
        ),

        onClick = onClick
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(28.dp)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = label,
                color = activeIcon,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ReminderCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),

        shape = RoundedCornerShape(32.dp),

        colors = CardDefaults.cardColors(
            containerColor = backgroundCardLight
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = brightGreen,
                        shape = CircleShape
                    ),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    painter = painterResource(
                        R.drawable.ic_lamp
                    ),
                    contentDescription = "Lamp",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = "Don't forget to save electricity today! Every little bit helps the planet.",

                modifier = Modifier.weight(1f),

                style = MaterialTheme.typography.bodyLarge,

                color = textColor
            )
        }
    }
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

    val progress =
        if (total == 0)
            0f
        else
            completed.toFloat() / total


    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 1000
        ),
        label = ""
    )

    val motivationText = when {
        progress >= 1f ->
            "Amazing! All habits completed"

        progress >= 0.8f ->
            "Almost there!"

        progress >= 0.5f ->
            "Great progress"

        progress >= 0.2f ->
            "Keep going"

        else ->
            "Let's get started"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "Daily Progress",
                    style =
                        MaterialTheme.typography.titleLarge,
                    color = titleTextColor,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    shape = RoundedCornerShape(
                        50.dp
                    ),

                    colors = CardDefaults.cardColors(
                        containerColor = brightGreen
                    )
                ) {

                    Text(
                        text = "Today",

                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 6.dp
                        ),

                        color = Color.White,

                        style =
                            MaterialTheme.typography.bodyMedium,

                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            HabitCircularProgress(
                modifier = Modifier.size(90.dp),
                progress = animatedProgress
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = motivationText,

                style =
                    MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text =
                    "You've completed $completed out of $total daily habits",

                style =
                    MaterialTheme.typography.bodyMedium,

                color = brightGreen
            )
        }
    }
}