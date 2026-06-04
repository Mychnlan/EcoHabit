package com.team4.ecohabit

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.team4.ecohabit.auth.GoogleAuthManager
import com.team4.ecohabit.components.BottomNavigationBar
import com.team4.ecohabit.components.HeaderBar
import com.team4.ecohabit.components.PageLoading
import com.team4.ecohabit.data.SessionManager
import com.team4.ecohabit.model.Habit
import com.team4.ecohabit.navigation.NavItem
import com.team4.ecohabit.navigation.Routes
import com.team4.ecohabit.screens.AddHabitScreen
import com.team4.ecohabit.screens.HabitScreen
import com.team4.ecohabit.screens.HistoryScreen
import com.team4.ecohabit.screens.HomeScreen
import com.team4.ecohabit.screens.ProfileScreen
import com.team4.ecohabit.screens.auth.LoginScreen
import com.team4.ecohabit.screens.auth.RegisterScreen
import com.team4.ecohabit.ui.theme.EcoHabitTheme
import com.team4.ecohabit.ui.theme.softGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            requestPermissions(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                100
            )
        }


        setContent {
            EcoHabitTheme {
                EcoHabitApp()
            }
        }
    }
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "habit_channel",
                "Habit Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager = getSystemService(
                NotificationManager::class.java
            )

            manager.createNotificationChannel(channel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EcoHabitApp() {

    val context = LocalContext.current

    val googleAuthManager = remember {
        GoogleAuthManager(context)
    }

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

        topBar = {

            if (currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER) {

                HeaderBar(
                    isBackButton =
                        currentRoute == Routes.ADD_HABIT ||
                                currentRoute == Routes.EDIT_HABIT ||
                                currentRoute == Routes.HISTORY,

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        },

        bottomBar = {

            if (
                currentRoute != Routes.LOGIN &&
                currentRoute != Routes.REGISTER &&
                currentRoute != Routes.ADD_HABIT &&
                currentRoute != Routes.EDIT_HABIT &&
                currentRoute != Routes.HISTORY
            ) {
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

                composable(Routes.LOGIN) {

                    LoginScreen(

                        onLoginSuccess = {

                            scope.launch {

                                isPageLoading = true

                                delay(300)

                                sessionManager.saveLogin()

                                delay(1200)

                                navController.navigate(Routes.HOME) {

                                    popUpTo(Routes.LOGIN) {
                                        inclusive = true
                                    }
                                }

                                isPageLoading = false
                            }
                        },

                        onRegisterClick = {

                            navController.navigate(Routes.REGISTER)
                        },

                        onGoogleSignIn = {

                            runCatching {

                                val user =
                                    googleAuthManager.signIn()

                                if (user != null) {

                                    sessionManager.saveLogin()

                                    navController.navigate(
                                        Routes.HOME
                                    ) {

                                        popUpTo(
                                            Routes.LOGIN
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        }
                    )
                }

                composable(Routes.REGISTER) {

                    RegisterScreen(

                        onRegisterSuccess = {

                            scope.launch {

                                isPageLoading = true

                                delay(300)

                                sessionManager.saveLogin()

                                delay(1200)

                                navController.navigate(Routes.HOME) {

                                    popUpTo(Routes.REGISTER) {
                                        inclusive = true
                                    }
                                }

                                isPageLoading = false
                            }
                        },

                        onLoginClick = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(Routes.HOME) {
                    HomeScreen(
                        onAddHabitClick = {
                            navController.navigate(Routes.ADD_HABIT)
                        }
                    )
                }

                composable(Routes.HABIT) {

                    HabitScreen(

                        onAddHabitClick = {
                            navController.navigate(Routes.ADD_HABIT)
                        },

                        onEditHabitClick = { habit ->

                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set(
                                    "edit_habit",
                                    habit
                                )

                            navController.navigate(
                                Routes.EDIT_HABIT
                            )
                        }
                    )
                }

                composable(Routes.PROFILE) {

                    ProfileScreen(
                        navController = navController
                    )
                }

                composable(Routes.HISTORY) {
                    HistoryScreen(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(Routes.ADD_HABIT) {

                    AddHabitScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSaveClick = {

                        }
                    )
                }

                composable(Routes.EDIT_HABIT) {

                    val habit =
                        navController
                            .previousBackStackEntry
                            ?.savedStateHandle
                            ?.get<Habit>("edit_habit")

                    if (habit != null) {

                        AddHabitScreen(

                            existingHabit = habit,

                            onBackClick = {
                                navController.popBackStack()
                            },

                            onSaveClick = {
                                navController.popBackStack()
                            }
                        )
                    }
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
