package com.team4.ecohabit.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.R
import com.team4.ecohabit.ui.theme.greenLogo
import com.team4.ecohabit.ui.theme.softGreen
import com.team4.ecohabit.ui.theme.textColor
import com.team4.ecohabit.ui.theme.white
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigate: () -> Unit
) {

    LaunchedEffect(Unit) {

        delay(2500)

        onNavigate()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(softGreen),

        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                shape = CircleShape,

                colors = CardDefaults.cardColors(
                    containerColor = white
                ),

                modifier = Modifier.size(220.dp)
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Image(
                        painter = painterResource(
                            R.drawable.ic_leaf
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            Text(
                text = "EcoHabit",
                color = greenLogo,
                style =
                    MaterialTheme.typography.headlineLarge
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Small Habits, Big Impact 🌍",
                color = textColor,
                style =
                    MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(80.dp)
            )

            CircularProgressIndicator(
                color = greenLogo
            )
        }
    }
}

