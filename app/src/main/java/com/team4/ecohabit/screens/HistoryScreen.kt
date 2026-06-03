package com.team4.ecohabit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.team4.ecohabit.components.HistoryTimeline
import com.team4.ecohabit.model.HistoryItem
import com.team4.ecohabit.model.HistoryRepository
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.softGreen
import com.team4.ecohabit.ui.theme.textColor

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit
) {

    val userId =
        FirebaseAuth.getInstance()
            .currentUser
            ?.uid

    var histories by remember {
        mutableStateOf<List<HistoryItem>>(emptyList())
    }

    LaunchedEffect(userId) {

        if (userId == null) return@LaunchedEffect

        HistoryRepository.getHistory(
            userId = userId,

            onResult = {
                histories = it
            },

            onError = {
                it.printStackTrace()
            }
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
            text = "Activity History",
            style = MaterialTheme.typography.headlineMedium,
            color = brightGreen
        )

        Text(
            text = "Track your eco-friendly habits and progress.",
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        HistoryTimeline(
            histories = histories
        )
    }
}