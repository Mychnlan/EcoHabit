package com.team4.ecohabit.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.team4.ecohabit.R
import com.team4.ecohabit.components.CategorySection
import com.team4.ecohabit.components.DailyTargetCard
import com.team4.ecohabit.components.EcoCard
import com.team4.ecohabit.components.HabitNameField
import com.team4.ecohabit.components.RepeatCard
import com.team4.ecohabit.helper.ReminderPermissionHelper
import com.team4.ecohabit.helper.ReminderScheduler
import com.team4.ecohabit.model.Habit
import com.team4.ecohabit.model.HabitRepository
import com.team4.ecohabit.model.RepeatDay
import com.team4.ecohabit.receiver.HabitReminderReceiver
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.greenLogo
import com.team4.ecohabit.ui.theme.softGreen
import com.team4.ecohabit.ui.theme.textColor
import com.team4.ecohabit.ui.theme.white

@Composable
fun AddHabitScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,

    existingHabit: Habit? = null
) {
    val context = LocalContext.current
    var habitName by remember {
        mutableStateOf(
            existingHabit?.name ?: ""
        )
    }
    var selectedCategory by remember {
        mutableStateOf(
            existingHabit?.category ?: "Energy"
        )
    }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var reminderHour by remember {
        mutableIntStateOf(
            existingHabit?.reminderHour ?: 8
        )
    }

    var reminderMinute by remember {
        mutableIntStateOf(
            existingHabit?.reminderMinute ?: 0
        )
    }

    var selectedDays by remember {

        mutableStateOf(

            existingHabit?.selectedDays
                ?.mapNotNull {

                    runCatching {
                        RepeatDay.valueOf(it)
                    }.getOrNull()

                }?.toSet()

                ?: setOf(
                    RepeatDay.MONDAY,
                    RepeatDay.TUESDAY,
                    RepeatDay.WEDNESDAY,
                    RepeatDay.THURSDAY,
                    RepeatDay.FRIDAY
                )
        )
    }

    val categories = listOf(
        "Recycling",
        "Energy",
        "Water",
        "Mindfulness",
        "Diet"
    )

    val icons = listOf(
        R.drawable.ic_leaf,
        R.drawable.ic_flower,
        R.drawable.ic_drop,
        R.drawable.ic_tree,
        R.drawable.ic_energy
    )

    var selectedIcon by remember {
        mutableIntStateOf(
            existingHabit?.icon ?: icons.first()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(softGreen)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text =
                if (existingHabit == null)
                    "Create New Habit"
                else
                    "Edit Habit",
            style = MaterialTheme.typography.headlineMedium,
            color = brightGreen
        )

        Text(
            text = "Plant a new seed for a better tomorrow.",
            color = textColor,
            fontWeight = FontWeight.Light,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(32.dp))

        HabitNameField(
            value = habitName,
            onValueChange = {
                habitName = it
            }
        )

        Spacer(Modifier.height(24.dp))

        CategorySection(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = {
                selectedCategory = it
            }
        )


        Spacer(Modifier.height(24.dp))

        IconSection(
            icons = icons,
            selectedIcon = selectedIcon,
            onSelected = {
                selectedIcon = it
            }
        )

        Spacer(Modifier.height(24.dp))

        ReminderSection(
            hour = reminderHour,
            minute = reminderMinute,
            onTimeSelected = { hour, minute ->
                reminderHour = hour
                reminderMinute = minute
            }
        )

        Spacer(Modifier.height(16.dp))

        RepeatCard(
            selectedDays = selectedDays,
            onDayClick = { day ->

                selectedDays =
                    if (day in selectedDays) {
                        selectedDays - day
                    } else {
                        selectedDays + day
                    }
            }
        )

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {
                if (
                    !ReminderPermissionHelper
                        .hasExactAlarmPermission(context)
                ) {

                    ReminderPermissionHelper
                        .requestExactAlarmPermission(context)

                    return@Button
                }
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button

                if (habitName.isBlank()) {
                    return@Button
                }

                isLoading = true

                val habit = Habit(
                    userId = userId,
                    name = habitName.trim(),
                    category = selectedCategory,
                    icon = selectedIcon,
                    selectedDays = selectedDays.map { it.name },
                    reminderHour = reminderHour,
                    reminderMinute = reminderMinute,
                    completedDates = emptyList()
                )

                if(existingHabit == null){
                    HabitRepository.checkHabitExists(
                        userId = userId,
                        habitName = habitName
                    ) { exists ->

                        if (exists) {

                            isLoading = false

                            return@checkHabitExists
                        }

                        HabitRepository.addHabit(
                            habit = habit,

                            onSuccess = {

                                isLoading = false
                                showSuccessDialog = true

                                ReminderScheduler.scheduleReminder(
                                    context = context,
                                    habitName = habit.name,
                                    hour = reminderHour,
                                    minute = reminderMinute
                                )
                            },

                            onFailure = {

                                isLoading = false
                                it.printStackTrace()
                            }
                        )
                    }
                } else {
                    HabitRepository.updateHabit(
                        habit = habit.copy(
                            id = existingHabit.id,
                            createdAt = existingHabit.createdAt,
                            completedDates = existingHabit.completedDates
                        ),

                        onSuccess = {
                            isLoading = false
                            onSaveClick()
                        },

                        onFailure = {
                            isLoading = false
                        }
                    )
                }

            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = brightGreen
            ),
            shape = RoundedCornerShape(32.dp)
        ) {
            Text(
                text =
                    if (isLoading)
                        "Saving..."
                    else if (existingHabit == null)
                        "Save Habit"
                    else
                        "Update Habit",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(24.dp))
    }

    if (showSuccessDialog) {

        AlertDialog(
            onDismissRequest = {},

            shape = RoundedCornerShape(24.dp),

            containerColor = white,

            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "🌱 Habit Created!",
                        color = brightGreen,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            text = {
                Text(
                    text = "Your eco habit has been successfully added. Would you like to create another habit or return to the home screen?",
                    color = textColor,
                    fontSize = 16.sp
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        habitName = ""

                        selectedCategory = "Energy"

                        selectedDays = setOf(
                            RepeatDay.MONDAY,
                            RepeatDay.TUESDAY,
                            RepeatDay.WEDNESDAY,
                            RepeatDay.THURSDAY,
                            RepeatDay.FRIDAY
                        )

                        selectedIcon = icons.first()

                        showSuccessDialog = false
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = brightGreen
                    ),

                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Add Another")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        showSuccessDialog = false
                        onBackClick()
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = greenLogo
                    ),

                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Back Home")
                }
            }
        )
    }
}

@Composable
private fun IconSection(
    icons: List<Int>,
    selectedIcon: Int,
    onSelected: (Int) -> Unit
) {

    Column {

        Text("Eco Icon")

        Spacer(Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(icons) { icon ->

                Surface(
                    onClick = {
                        onSelected(icon)
                    },
                    shape = CircleShape,
                    border = BorderStroke(
                        width = if (selectedIcon == icon) 2.dp else 1.dp,
                        color = if (selectedIcon == icon)
                            brightGreen
                        else
                            Color.LightGray
                    ),
                    color = white
                ) {

                    Image(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(18.dp)
                            .size(28.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderSection(
    hour: Int,
    minute: Int,
    onTimeSelected: (Int, Int) -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    EcoCard {

        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(R.drawable.ic_clock),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = "Reminder",
                    color = brightGreen,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = greenLogo
                )
            ) {

                Text(
                    String.format(
                        "%02d:%02d",
                        hour,
                        minute
                    )
                )
            }
        }
    }

    if (showDialog) {

        val state = rememberTimePickerState(
            initialHour = hour,
            initialMinute = minute,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },

            text = {
                TimePicker(state = state)
            },

            confirmButton = {

                Button(
                    onClick = {

                        onTimeSelected(
                            state.hour,
                            state.minute
                        )

                        showDialog = false
                    }
                ) {
                    Text("Save")
                }
            }
        )
    }
}

