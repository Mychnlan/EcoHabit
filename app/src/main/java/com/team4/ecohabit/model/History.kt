package com.team4.ecohabit.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.time.LocalDate

data class HistoryItem(
    val id: String = "",
    val habitId: String = "",
    val habitName: String = "",
    val icon: Int = 0,
    val points: Int = 10,
    val completedAt: Long = System.currentTimeMillis()
)

object HistoryRepository {

    private val firestore =
        FirebaseFirestore.getInstance()

    fun getHistory(

        userId: String,

        onResult: (List<HistoryItem>) -> Unit,

        onError: (Exception) -> Unit

    ) {

        firestore.collection("users")
            .document(userId)
            .collection("histories")
            .orderBy(
                "completedAt",
                Query.Direction.DESCENDING
            )
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val histories =
                    snapshot?.toObjects(
                        HistoryItem::class.java
                    ) ?: emptyList()

                onResult(histories)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addOrUpdateHistory(
        userId: String,
        history: HistoryItem
    ) {

        val today =
            LocalDate.now().toString()

        val documentId =
            "${history.habitId}_$today"

        firestore.collection("users")
            .document(userId)
            .collection("histories")
            .document(documentId)
            .set(
                history.copy(
                    id = documentId
                )
            )
    }
}