package com.team4.ecohabit.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.parcelize.Parcelize

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

@Parcelize
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
): Parcelable

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

    fun updateHabit(
        habit: Habit,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {

        firestore.collection("users")
            .document(habit.userId)
            .collection("habits")
            .document(habit.id)
            .set(habit)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
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

    fun deleteHabit(
        userId: String,
        habitId: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {

        firestore.collection("users")
            .document(userId)
            .collection("habits")
            .document(habitId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    fun checkHabitExists(
        userId: String,
        habitName: String,
        onResult: (Boolean) -> Unit
    ) {

        firestore.collection("users")
            .document(userId)
            .collection("habits")
            .whereEqualTo(
                "searchName",
                habitName.trim()
            )
            .get()
            .addOnSuccessListener {

                onResult(
                    !it.isEmpty
                )
            }
            .addOnFailureListener {

                onResult(false)
            }
    }
}