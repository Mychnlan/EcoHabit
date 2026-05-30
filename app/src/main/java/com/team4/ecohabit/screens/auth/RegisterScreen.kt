package com.team4.ecohabit.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.R
import com.team4.ecohabit.auth.FirebaseAuthManager
import com.team4.ecohabit.components.AuthTextField
import com.team4.ecohabit.ui.theme.backgroundCardLight
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.greenLogo
import com.team4.ecohabit.ui.theme.softGreen
import com.team4.ecohabit.ui.theme.textColor
import com.team4.ecohabit.ui.theme.titleTextColor
import com.team4.ecohabit.ui.theme.white
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit, onLoginClick: () -> Unit
) {

    var fullName by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var showSuccessDialog by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(softGreen)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 22.dp, vertical = 22.dp
                ),

            shape = RoundedCornerShape(42.dp),

            colors = CardDefaults.cardColors(
                containerColor = white
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = 28.dp,
                        vertical = 34.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .background(
                            color = backgroundCardLight, shape = CircleShape
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        painter = painterResource(
                            R.drawable.ic_leaf
                        ),
                        contentDescription = null,
                        tint = greenLogo,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineLarge,
                    color = titleTextColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Start your eco-friendly journey today",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                AuthTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Full Name",
                    placeholder = "John Doe",
                    icon = R.drawable.ic_user
                )

                Spacer(modifier = Modifier.height(18.dp))

                AuthTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    placeholder = "you@example.com",
                    icon = R.drawable.ic_email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(18.dp))

                AuthTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    placeholder = "••••••••",
                    icon = R.drawable.ic_lock,
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                AuthTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password",
                    placeholder = "••••••••",
                    icon = R.drawable.ic_lock,
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                AnimatedVisibility(
                    visible = errorMessage.isNotEmpty(), enter = fadeIn(), exit = fadeOut()
                ) {

                    Text(
                        text = errorMessage,

                        color = Color.Red,

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {

                        focusManager.clearFocus()

                        errorMessage = ""

                        scope.launch {

                            when {

                                fullName.isBlank() -> errorMessage = "Full name is required"

                                email.isBlank() -> errorMessage = "Email is required"

                                password.length < 6 -> errorMessage =
                                    "Password must be at least 6 characters"

                                password != confirmPassword -> errorMessage =
                                    "Password confirmation doesn't match"

                                else -> {

                                    isLoading = true

                                    FirebaseAuthManager.register(
                                        fullName = fullName,
                                        email = email,
                                        password = password,
                                        onSuccess = {
                                            isLoading = false
                                            showSuccessDialog = true
                                        },

                                        onError = {
                                            isLoading = false
                                            errorMessage = it
                                        }
                                    )
                                }
                            }
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    enabled = !isLoading,

                    shape = RoundedCornerShape(14.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = brightGreen, disabledContainerColor = brightGreen
                    )
                ) {
                    if (showSuccessDialog) {
                        AlertDialog(
                            onDismissRequest = {},

                            title = {
                                Text("Registration Successful")
                            },

                            text = {
                                Text(
                                    "A verification email has been sent to your email address. Please verify your account before logging in."
                                )
                            },

                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showSuccessDialog = false
                                        onLoginClick()
                                    }
                                ) {
                                    Text("OK")
                                }
                            }
                        )
                    }

                    if (isLoading) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 3.dp
                        )

                    } else {
                        Text(
                            text = "Create Account", style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account?", color = textColor
                    )

                    TextButton(
                        onClick = onLoginClick
                    ) {

                        Text(
                            text = "Login",
                            color = brightGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}