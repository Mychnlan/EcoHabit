package com.team4.ecohabit.components

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.R
import com.team4.ecohabit.ui.theme.backgroundCardLight
import com.team4.ecohabit.ui.theme.grayGreen
import com.team4.ecohabit.ui.theme.white

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = white
            ),
            shape = RoundedCornerShape(32.dp)
        ) {

            androidx.compose.foundation.layout.Column(
                modifier = Modifier.padding(28.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            color = grayGreen,
                            shape = CircleShape
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        painter = painterResource(
                            R.drawable.ic_logout
                        ),

                        contentDescription = null,

                        tint = Color.Red,

                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Log Out",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Are you sure you want to leave EcoHabit?",

                    textAlign = TextAlign.Center,

                    color = Color.Gray,

                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedButton(
                        onClick = onDismiss,

                        modifier = Modifier.weight(1f),

                        shape = RoundedCornerShape(100.dp)
                    ) {

                        Text(
                            text = "Cancel",
                            color = Color.Black
                        )
                    }

                    Button(
                        onClick = onLogout,

                        modifier = Modifier.weight(1f),

                        shape = RoundedCornerShape(100.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {

                        Text("Log Out")
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteHabitDialog(
    habitName: String,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(32.dp),

            colors = CardDefaults.cardColors(
                containerColor = white
            )
        ) {

            Column(
                modifier = Modifier.padding(28.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            Color.Red.copy(alpha = 0.1f),
                            CircleShape
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        painter = painterResource(R.drawable.ic_trash),
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Delete Habit",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Are you sure you want to delete \"$habitName\"?",
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(100.dp)
                    ) {

                        Text("Cancel")
                    }

                    Button(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),

                        shape = RoundedCornerShape(100.dp)
                    ) {

                        Text("Delete")
                    }
                }
            }
        }
    }
}
