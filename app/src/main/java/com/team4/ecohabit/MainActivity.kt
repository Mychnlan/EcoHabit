package com.team4.ecohabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.team4.ecohabit.components.BottomNavigationBar
import com.team4.ecohabit.components.HeaderBar
import com.team4.ecohabit.navigation.NavItem
import com.team4.ecohabit.screens.HabitScreen
import com.team4.ecohabit.screens.HomeScreen
import com.team4.ecohabit.screens.ProfileScreen
import com.team4.ecohabit.screens.ProgressScreen
import com.team4.ecohabit.ui.theme.EcoHabitTheme
import com.team4.ecohabit.ui.theme.softGreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EcoHabitTheme {
                EcoHabitApp()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun EcoHabitApp() {

    val navController = rememberNavController()

    val items = listOf(
        NavItem.Home,
        NavItem.Habit,
        NavItem.Profile
    )

    var isLoading by remember {
        mutableStateOf(false)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry) {

        isLoading = true

        delay(450)

        isLoading = false
    }

    Scaffold(
        containerColor = softGreen,

        bottomBar = {
            BottomNavigationBar(
                navController,
                items
            )
        }

    ) { padding ->

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            AnimatedContent(
                targetState = isLoading,

                transitionSpec = {

                    fadeIn(
                        animationSpec = tween(300)
                    ) togetherWith fadeOut(
                        animationSpec = tween(300)
                    )
                },

                label = ""
            ) { loading ->

                if (loading) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(softGreen),

                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            color = Color(0xFF0A6B18),
                            strokeWidth = 4.dp
                        )
                    }

                } else {

                    NavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 64.dp)
                            .padding(padding),

                        navController = navController,

                        startDestination = "home"
                    ) {

                        composable("home") {
                            HomeScreen()
                        }

                        composable("habit") {
                            HabitScreen()
                        }

                        composable("profile") {
                            ProfileScreen()
                        }
                    }
                }
            }

            HeaderBar(
                modifier = Modifier.align(
                    Alignment.TopCenter
                )
            )
        }
    }
}