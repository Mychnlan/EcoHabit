package com.team4.ecohabit.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.R
import com.team4.ecohabit.data.SessionManager
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.grayGreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.team4.ecohabit.components.LogoutDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val sessionManager = remember {
        SessionManager(context)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),

        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            ProfileCard()
        }

        item {
            SettingsCard(
                sessionManager = sessionManager,
                navController = navController,
                scope = scope
            )
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ProfileCard() {

    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth(),

        shape = RoundedCornerShape(28.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.ic_leaf),
                contentDescription = "Profile",

                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape),

                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Alex Morgan",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "alex.morgan@example.com",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                shape = RoundedCornerShape(24.dp),

                colors = CardDefaults.cardColors(
                    containerColor = brightGreen
                )
            ) {

                Column(
                    modifier = Modifier.padding(
                        horizontal = 32.dp,
                        vertical = 20.dp
                    ),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "TOTAL POINTS",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "4,250",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "+120 this week",
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun SettingsCard(
    sessionManager: SessionManager,
    navController: NavController,
    scope: CoroutineScope
) {
    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),

        shape = RoundedCornerShape(28.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        )
    ) {

        Column {

            Text(
                text = "Account Settings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,

                modifier = Modifier.padding(20.dp)
            )

            HorizontalDivider()

            SettingItem(
                icon = R.drawable.ic_user,
                title = "Personal Information",
                subtitle = "Update your name, email"
            )

            HorizontalDivider()

            SettingItem(
                icon = R.drawable.ic_notification, //ic_notification
                title = "Notifications",
                subtitle = "Manage habit reminders and alerts"
            )

            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {

                        showLogoutDialog = true
                    }
                    .padding(20.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                CircleIcon(
                    icon = R.drawable.ic_logout,
                    tint = Color.Red
                )

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "Log Out",
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    if (showLogoutDialog) {

        LogoutDialog(

            onDismiss = {

                showLogoutDialog = false
            },

            onLogout = {

                scope.launch {

                    showLogoutDialog = false

                    sessionManager.logout()

                    navController.navigate("login") {

                        popUpTo(0)
                    }
                }
            }
        )
    }
}

@Composable
fun SettingItem(
    @DrawableRes icon: Int,
    title: String,
    subtitle: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        CircleIcon(icon)

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = subtitle,
                color = Color.DarkGray
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_chevron),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun CircleIcon(
    @DrawableRes icon: Int,
    tint: Color = Color.Black
) {

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(grayGreen),

        contentAlignment = Alignment.Center
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}