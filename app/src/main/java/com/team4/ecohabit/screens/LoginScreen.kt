package com.team4.ecohabit.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.R
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.buttonAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4FAF2)
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        Card(
            modifier = Modifier
                .padding(
                    horizontal = 22.dp,
                    vertical = 28.dp
                )
                .fillMaxSize(),

            shape = RoundedCornerShape(42.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                            color = Color(0xFFE8EFE5),
                            shape = CircleShape
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        painter = painterResource(
                            R.drawable.ic_leaf
                        ),

                        contentDescription = null,

                        tint = Color(0xFF056B13),

                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Welcome Back",

                    style = MaterialTheme.typography.displaySmall,

                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Continue your eco journey.",

                    style = MaterialTheme.typography.headlineSmall,

                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Email Address",

                        style = MaterialTheme.typography.titleMedium,

                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,

                        onValueChange = {
                            email = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        singleLine = true,

                        leadingIcon = {

                            Icon(
                                painter = painterResource(
                                    R.drawable.ic_email
                                ),

                                contentDescription = null,

                                tint = Color.Gray
                            )
                        },

                        placeholder = {

                            Text(
                                text = "you@example.com"
                            )
                        },

                        keyboardOptions = KeyboardOptions(
                            keyboardType =
                                KeyboardType.Email
                        ),

                        shape = RoundedCornerShape(0.dp),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF056B13),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Password",

                        style =
                            MaterialTheme.typography.titleMedium,

                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,

                        onValueChange = {
                            password = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        singleLine = true,

                        visualTransformation =
                            PasswordVisualTransformation(),

                        leadingIcon = {

                            Icon(
                                painter = painterResource(
                                    R.drawable.ic_lock
                                ),

                                contentDescription = null,

                                tint = Color.Gray
                            )
                        },

                        placeholder = {

                            Text(
                                text = "••••••••"
                            )
                        },

                        keyboardOptions = KeyboardOptions(
                            keyboardType =
                                KeyboardType.Password
                        ),

                        shape = RoundedCornerShape(0.dp),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF056B13),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                }

                AnimatedVisibility(
                    visible = errorMessage.isNotEmpty(),

                    enter = fadeIn(),

                    exit = fadeOut()
                ) {

                    Text(
                        text = errorMessage,

                        color = Color.Red,

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),

                        textAlign = TextAlign.Start
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {

                        focusManager.clearFocus()

                        errorMessage = ""

                        scope.launch {

                            isLoading = true

                            delay(1500)

                            if (
                                email == "admin@gmail.com" &&
                                password == "123456"
                            ) {

                                onLoginSuccess()

                            } else {

                                errorMessage =
                                    "Invalid email or password"
                            }

                            isLoading = false
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(12.dp)
                        ),

                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                            containerColor = brightGreen,
                        disabledContainerColor = brightGreen,
                        disabledContentColor = Color.White
                    ),

                    shape = RoundedCornerShape(12.dp),
                ) {

                    if (isLoading) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(26.dp),

                            color = Color.White,

                            strokeWidth = 3.dp
                        )

                    } else {

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Ready to go!",
                                style = MaterialTheme.typography.headlineSmall
                            )

                            Spacer(
                                modifier = Modifier.width(12.dp)
                            )

                            Icon(
                                painter = painterResource(
                                    R.drawable.ic_chevron
                                ),

                                contentDescription = null,

                                tint = Color.White,

                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),

                        color = Color(0xFFDCE4D9)
                    )

                    Text(
                        text = "or",

                        modifier = Modifier.padding(
                            horizontal = 18.dp
                        ),

                        color = Color.Gray
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),

                        color = Color(0xFFDCE4D9)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement =
                        Arrangement.Center,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "New to EcoHabit?",

                        color = Color.Gray
                    )

                    TextButton(
                        onClick = {

                        }
                    ) {

                        Text(
                            text = "Create an account",

                            color = brightGreen,

                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}