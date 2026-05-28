package com.team4.ecohabit.navigation

import androidx.annotation.DrawableRes
import com.team4.ecohabit.R

sealed class NavItem(

    val route:String,
    val title:String,

    @DrawableRes
    val icon:Int
){

    object Home:NavItem(
        "home",
        "Home",
        R.drawable.ic_home
    )

    object Habit:NavItem(
        "habit",
        "Habit",
        R.drawable.ic_habit
    )

    object Progress:NavItem(
        "progress",
        "Progress",
        R.drawable.ic_progres
    )

    object Profile:NavItem(
        "profile",
        "Profile",
        R.drawable.ic_user
    )
}