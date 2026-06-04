package com.team4.ecohabit.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.team4.ecohabit.R
import com.team4.ecohabit.helper.ReminderScheduler
import com.team4.ecohabit.model.Habit
import com.team4.ecohabit.ui.theme.grayGreen
import com.team4.ecohabit.ui.theme.greenLogo
import com.team4.ecohabit.ui.theme.white
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CheckboxItemHabit(
    habits: List<Habit>,
    onCheckedChange: (Habit, Boolean) -> Unit,
    onDeleteHabit: (Habit) -> Unit,
    onEditHabit: (Habit) -> Unit
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var expandedHabitId by remember {
        mutableStateOf<String?>(null)
    }

    var selectedHabit by remember {
        mutableStateOf<Habit?>(null)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }


    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            habits.forEachIndexed { index, habit ->

                val today =
                    LocalDate.now().toString()

                var isChecked by remember(
                    habit.id,
                    habit.completedDates
                ) {
                    mutableStateOf(
                        today in habit.completedDates
                    )
                }

                val cardColor = animateColorAsState(
                    targetValue =
                        if (isChecked) greenLogo
                        else white,

                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    ),

                    label = "cardColor"
                )

                val textColor = animateColorAsState(
                    targetValue =
                        if (isChecked) Color.White
                        else Color.Black,

                    animationSpec = tween(400),

                    label = "textColor"
                )

                val iconBackground = animateColorAsState(
                    targetValue =
                        if (isChecked) Color.White
                        else grayGreen,

                    animationSpec = tween(400),

                    label = "iconBackground"
                )

                val scaleAnimation = animateFloatAsState(
                    targetValue =
                        if (isChecked) 0.97f
                        else 1f,

                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),

                    label = "scaleAnimation"
                )

                val textAlpha = animateFloatAsState(
                    targetValue =
                        if (isChecked) 0.75f
                        else 1f,

                    animationSpec = tween(400),

                    label = "textAlpha"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(scaleAnimation.value)
                        .combinedClickable(
                            onClick = {},
                            onLongClick = {
                                selectedHabit = habit
                                expandedHabitId = habit.id
                            }
                        )
                        .shadow(
                            elevation =
                                if (isChecked) 4.dp
                                else 12.dp,

                            shape = RoundedCornerShape(40.dp),

                            clip = false
                        ),

                    shape = RoundedCornerShape(40.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = cardColor.value
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(
                                horizontal = 20.dp,
                                vertical = 20.dp
                            )
                            .fillMaxWidth(),

                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = iconBackground.value,
                                        shape = CircleShape
                                    ),

                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    painter = painterResource(habit.icon),
                                    contentDescription = "Lamp",
                                    tint = greenLogo,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = habit.name,
                                maxLines = 2,

                                color = textColor.value,

                                textDecoration =
                                    if (isChecked)
                                        TextDecoration.LineThrough
                                    else
                                        TextDecoration.None,

                                modifier = Modifier.graphicsLayer {
                                    alpha = textAlpha.value
                                }
                            )
                        }

                        Box {
                            CircleCheckbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->

                                    isChecked = checked

                                    onCheckedChange(
                                        habit,
                                        checked
                                    )
                                }
                            )

                            DropdownMenu(
                                expanded = expandedHabitId == habit.id,

                                onDismissRequest = {
                                    expandedHabitId = null
                                },

                                shape = RoundedCornerShape(20.dp),

                                containerColor = white
                            ) {

                                DropdownMenuItem(
                                    text = {
                                        Text("Edit")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_edit),
                                            contentDescription = null
                                        )
                                    },
                                    onClick = {
                                        expandedHabitId = null
                                        onEditHabit(habit)
                                    }
                                )

                                HorizontalDivider()

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "Delete",
                                            color = Color.Red
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_trash),
                                            contentDescription = null,
                                            tint = Color.Red
                                        )
                                    },
                                    onClick = {
                                        selectedHabit = habit
                                        expandedHabitId = null
                                        showDeleteDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog && selectedHabit != null) {
        val context = LocalContext.current
        DeleteHabitDialog(

            habitName = selectedHabit!!.name,

            onDismiss = {
                showDeleteDialog = false
            },

            onDelete = {
                ReminderScheduler.cancelReminder(
                    context = context,
                    habitName = selectedHabit!!.name
                )

                selectedHabit?.let {

                    onDeleteHabit(it)
                }

                showDeleteDialog = false
            }
        )
    }
}