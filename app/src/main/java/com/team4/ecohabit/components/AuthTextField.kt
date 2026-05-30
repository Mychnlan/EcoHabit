package com.team4.ecohabit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.ui.theme.activeIcon
import com.team4.ecohabit.ui.theme.brightGreen
import com.team4.ecohabit.ui.theme.grayGreen
import com.team4.ecohabit.ui.theme.titleTextColor
import com.team4.ecohabit.ui.theme.white

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = label,

            style = MaterialTheme.typography.bodyLarge,

            color = titleTextColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,

            onValueChange = onValueChange,

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            leadingIcon = {

                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = activeIcon
                )
            },

            placeholder = {
                Text(placeholder)
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),

            visualTransformation =
                if (isPassword)
                    PasswordVisualTransformation()
                else
                    androidx.compose.ui.text.input.VisualTransformation.None,

            shape = RoundedCornerShape(14.dp),

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = brightGreen,
                unfocusedBorderColor = grayGreen,
                focusedContainerColor = white,
                unfocusedContainerColor = white
            )
        )
    }
}