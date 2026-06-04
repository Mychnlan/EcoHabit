package com.team4.ecohabit.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.data.onboardingPages
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.grayGreen
import com.team4.ecohabit.ui.theme.greenLogo
import com.team4.ecohabit.ui.theme.softGreen
import com.team4.ecohabit.ui.theme.textColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {

    val pagerState = rememberPagerState(
        pageCount = {
            onboardingPages.size
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(softGreen)
            .padding(24.dp)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->

            val item =
                onboardingPages[page]

            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Center,

                modifier =
                    Modifier.fillMaxSize()
            ) {

                Image(
                    painter =
                        painterResource(item.image),

                    contentDescription = null,

                    modifier =
                        Modifier.size(280.dp)
                )

                Spacer(
                    modifier = Modifier.height(32.dp)
                )

                Text(
                    text = item.title,

                    style =
                        MaterialTheme.typography.headlineMedium,

                    color = greenLogo
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = item.description,

                    textAlign = TextAlign.Center,

                    color = textColor
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.Center
        ) {

            repeat(onboardingPages.size) { index ->

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(
                            if (pagerState.currentPage == index)
                                12.dp
                            else
                                8.dp
                        )
                        .background(
                            color =
                                if (pagerState.currentPage == index)
                                    brightGreen
                                else
                                    grayGreen,

                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = {

                if (
                    pagerState.currentPage ==
                    onboardingPages.lastIndex
                ) {

                    onFinished()

                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            shape =
                RoundedCornerShape(20.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor = brightGreen
                )
        ) {

            Text(

                if (
                    pagerState.currentPage ==
                    onboardingPages.lastIndex
                )
                    "Get Started"
                else
                    "Swipe →"
            )
        }
    }
}