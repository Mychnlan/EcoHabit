package com.team4.ecohabit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.R
import com.team4.ecohabit.ui.theme.checkedBox
import com.team4.ecohabit.ui.theme.white

@Composable
fun CircleCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                if (checked)
                    white
                else
                    Color.Transparent
            )
            .border(
                width = 2.dp,
                color = if (checked)
                   white
                else
                    Color.Gray,
                shape = CircleShape
            )
            .clickable {
                onCheckedChange(!checked)
            },
        contentAlignment = Alignment.Center
    ) {

        if (checked) {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_check
                ),
                contentDescription = "checked",
                modifier = Modifier.size(20.dp),
            )
        }
    }
}