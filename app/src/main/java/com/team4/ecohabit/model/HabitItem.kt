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
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val category: String = "",
    val icon: Int = 0,
    val selectedDays: List<String> = emptyList(),
    val reminderHour: Int = 8,
    val reminderMinute: Int = 0,
    val completedDates: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

object HabitRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun addHabit(
        habit: Habit,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val documentRef =
            firestore.collection("users")
                .document(habit.userId)
                .collection("habits")
                .document()

        val habitWithId =
            habit.copy(
                id = documentRef.id
            )

        documentRef
            .set(habitWithId)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    fun getHabits(
        userId: String,
        onResult: (List<Habit>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("users")
            .document(userId)
            .collection("habits")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val habits = snapshot?.documents?.map { doc ->

                    Habit(
                        id = doc.id,
                        userId = doc.getString("userId") ?: "",
                        name = doc.getString("name") ?: "",
                        category = doc.getString("category") ?: "",
                        icon = (doc.getLong("icon") ?: 0).toInt(),
                        selectedDays = doc.get("selectedDays") as? List<String> ?: emptyList(),
                        reminderHour = (doc.getLong("reminderHour") ?: 8).toInt(),
                        reminderMinute = (doc.getLong("reminderMinute") ?: 0).toInt(),
                        completedDates =
                            doc.get("completedDates") as? List<String>
                                ?: emptyList(),
                        createdAt = doc.getLong("createdAt") ?: 0L
                    )
                } ?: emptyList()

                onResult(habits)
            }
    }

    fun updateHabitToday(
        userId: String,
        habitId: String,
        completedDates: List<String>,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {

        firestore.collection("users")
            .document(userId)
            .collection("habits")
            .document(habitId)
            .update(
                "completedDates",
                completedDates
            )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }
}