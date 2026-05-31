package com.team4.ecohabit.model

import androidx.annotation.DrawableRes
import com.google.firebase.firestore.FirebaseFirestore

data class HabitItem(
    val id: Int,
    val title: String,
    val isChecked: Boolean = false,
    @DrawableRes val icon: Int
)

data class HabitCategory(
    val name: String
)

data class HabitIcon(
    @DrawableRes val icon: Int
)

data class Habit(
    val userId: String = "",
    val name: String = "",
    val category: String = "",
    val icon: Int = 0,
    val selectedDays: List<String> = emptyList(),
    val reminderHour: Int = 8,
    val reminderMinute: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

object HabitRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun addHabit(
        habit: Habit,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("users")
            .document(habit.userId)
            .collection("habits")
            .add(habit)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}