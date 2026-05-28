package com.team4.ecohabit.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.team4.ecohabit.navigation.NavItem
import com.team4.ecohabit.ui.theme.activeIcon
import com.team4.ecohabit.ui.theme.activeMenu
import com.team4.ecohabit.ui.theme.white
import kotlin.collections.forEach
import kotlin.div
import kotlin.times

@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<NavItem>
){
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),

            shape = RoundedCornerShape(25.dp, 25.dp, 0.dp, 0.dp),

            colors = CardDefaults.cardColors(
                containerColor = white
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp
            )
        ) {
            val currentRoute =
                navController.currentBackStackEntryAsState()
                    .value
                    ?.destination
                    ?.route

            val selectedIndex =
                items.indexOfFirst {
                    it.route == currentRoute
                }.coerceAtLeast(0)

            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()

            ) {

                val itemWidth = maxWidth / items.size

                val indicatorOffset by animateDpAsState(

                    targetValue =
                        itemWidth * selectedIndex +
                                (itemWidth - 70.dp) / 2,

                    animationSpec = spring(
                        dampingRatio =
                            Spring.DampingRatioMediumBouncy,

                        stiffness =
                            Spring.StiffnessLow
                    ),

                    label = ""
                )

                Box(
                    modifier = Modifier
                        .offset(
                            x = indicatorOffset,
                            y = 12.dp
                        )
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(activeMenu)
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    items.forEach { item ->

                        val selected =
                            currentRoute == item.route

                        Column(

                            modifier = Modifier
                                .padding(
                                    bottom = 20.dp
                                )
                                .weight(1f)
                                .fillMaxHeight()

                                .clickable(
                                    indication = null,

                                    interactionSource =
                                        remember {
                                            MutableInteractionSource()
                                        }
                                ) {

                                    navController.navigate(
                                        item.route
                                    ) {

                                        popUpTo(
                                            navController.graph.startDestinationId
                                        ) {
                                            saveState = true
                                        }

                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },

                            horizontalAlignment =
                                Alignment.CenterHorizontally,

                            verticalArrangement =
                                Arrangement.Center
                        ) {

                            Icon(
                                painter =
                                    painterResource(item.icon),

                                contentDescription =
                                    item.title,

                                tint =
                                    if(selected)
                                        activeIcon
                                    else
                                        Color.Gray
                            )

                            Spacer(
                                Modifier.height(4.dp)
                            )

                            Text(
                                text = item.title,

                                maxLines = 1,

                                style =
                                    MaterialTheme.typography
                                        .labelSmall,

                                color =
                                    if(selected)
                                        activeIcon
                                    else
                                        Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}