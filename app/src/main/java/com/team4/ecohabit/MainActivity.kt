package com.team4.ecohabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.team4.ecohabit.components.BottomNavigationBar
import com.team4.ecohabit.components.PageLoading
import com.team4.ecohabit.data.SessionManager
import com.team4.ecohabit.navigation.NavItem
import com.team4.ecohabit.screens.HabitScreen
import com.team4.ecohabit.screens.HomeScreen
import com.team4.ecohabit.screens.ProfileScreen
import com.team4.ecohabit.screens.auth.LoginScreen
import com.team4.ecohabit.ui.theme.EcoHabitTheme
import com.team4.ecohabit.ui.theme.softGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    val context = LocalContext.current

    val sessionManager = remember {
        SessionManager(context)
    }

    var startDestination by remember {
        mutableStateOf<String?>(null)
    }

    var isPageLoading by remember {
        mutableStateOf(false)
    }

    val navController = rememberNavController()

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        sessionManager
            .isLoginValid()
            .collect { valid ->

                startDestination =
                    if (valid) "home"
                    else "login"
            }
    }

    if (startDestination == null) {

        PageLoading()

        return
    }

    Scaffold(

        containerColor = softGreen,

        bottomBar = {

            if (currentRoute != "login") {

                BottomNavigationBar(
                    navController = navController,

                    items = listOf(
                        NavItem.Home,
                        NavItem.Habit,
                        NavItem.Profile
                    )
                )
            }
        }

    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            NavHost(
                navController = navController,

                startDestination = startDestination!!,

                modifier = Modifier.padding(padding)
            ) {

                composable("login") {

                    LoginScreen(

                        onLoginSuccess = {

                            scope.launch {

                                isPageLoading = true

                                delay(300)

                                sessionManager.saveLogin()

                                delay(1200)

                                navController.navigate("home") {

                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }

                                isPageLoading = false
                            }
                        }
                    )
                }

                composable("home") {
                    HomeScreen()
                }

                composable("habit") {
                    HabitScreen()
                }

                composable("profile") {

                    ProfileScreen(
                        navController = navController
                    )
                }
            }

            AnimatedVisibility(
                visible = isPageLoading,

                enter = fadeIn(
                    animationSpec = tween(250)
                ),

                exit = fadeOut(
                    animationSpec = tween(250)
                )
            ) {

                PageLoading()
            }
        }
    }
}