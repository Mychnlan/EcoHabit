package com.team4.ecohabit.model

import androidx.annotation.DrawableRes

data class HabitItem(
    val id: Int,
    val title: String,
    val isChecked: Boolean = false,
    @DrawableRes val icon: Int
)
