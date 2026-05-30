package com.team4.ecohabit.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.R
import com.team4.ecohabit.ui.theme.greenLogo
import com.team4.ecohabit.ui.theme.softGreen
import com.team4.ecohabit.ui.theme.textColor

@Preview(showBackground = true)
@Composable
fun HeaderBar(
    isBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(softGreen)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            AnimatedContent(
                targetState = isBackButton,

                transitionSpec = {

                    slideInHorizontally(
                        initialOffsetX = { it / 3 }
                    ) + fadeIn() togetherWith

                            slideOutHorizontally(
                                targetOffsetX = { -it / 3 }
                            ) + fadeOut()
                },

                label = "header_icon"
            ) { showBackButton ->

                if (showBackButton) {

                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            painter = painterResource(
                                R.drawable.ic_chevron
                            ),
                            contentDescription = null,
                            tint = greenLogo,
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = -1f
                                }
                                .size(30.dp)
                        )
                    }

                } else {

                    Icon(
                        painter = painterResource(
                            R.drawable.ic_leaf
                        ),
                        contentDescription = null,
                        tint = greenLogo,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Text(
                text = "EcoHabit",
                style = MaterialTheme.typography.headlineLarge,
                color = greenLogo
            )
        }
    }
}