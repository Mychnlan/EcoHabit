package com.team4.ecohabit.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team4.ecohabit.R
import com.team4.ecohabit.components.CategorySection
import com.team4.ecohabit.components.DailyTargetCard
import com.team4.ecohabit.components.EcoCard
import com.team4.ecohabit.components.HabitNameField
import com.team4.ecohabit.components.RepeatCard
import com.team4.ecohabit.model.RepeatDay
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.greenLogo
import com.team4.ecohabit.ui.theme.softGreen
import com.team4.ecohabit.ui.theme.textColor
import com.team4.ecohabit.ui.theme.white

@Composable
fun AddHabitScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {

    var habitName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Energy") }

    var selectedDays by remember {
        mutableStateOf(
            setOf(
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
        mutableIntStateOf(icons.first())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(softGreen)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Create New Habit",
            style = MaterialTheme.typography.headlineMedium,
            color = brightGreen
        )

        Text(
            text = "Plant a new seed for a better tomorrow.",
            color = textColor,
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

        ReminderSection()

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

        Spacer(Modifier.height(16.dp))

        DailyTargetCard()

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = brightGreen
            ),
            shape = RoundedCornerShape(32.dp)
        ) {
            Text(
                "Save Habit",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(24.dp))
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

@Composable
private fun ReminderSection() {

    EcoCard {

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
                "Reminder",
                color = brightGreen,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

