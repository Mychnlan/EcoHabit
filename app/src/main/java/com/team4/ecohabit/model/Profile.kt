package com.team4.ecohabit.model

import com.google.firebase.firestore.FirebaseFirestore
import kotlin.compareTo
import kotlin.text.get

data class ProfileStats(
    val totalPoints: Int = 0,
    val weeklyPoints: Int = 0
)

object ProfileRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun getProfileStats(
        userId: String,
        onResult: (ProfileStats) -> Unit
    ) {

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { userDoc ->

                val name =
                    userDoc.getString("name") ?: "Eco User"

                val email =
                    userDoc.getString("email") ?: ""

                firestore.collection("users")
                    .document(userId)
                    .collection("histories")
                    .get()
                    .addOnSuccessListener { historyDocs ->

                        val histories =
                            historyDocs.documents.mapNotNull {

                                runCatching {

                                    HistoryItem(
                                        points = (it.getLong("points")
                                            ?: 0).toInt(),

                                        completedAt =
                                            it.getLong("completedAt")
                                                ?: 0L
                                    )

                                }.getOrNull()
                            }

                        val totalPoints =
                            histories.sumOf {
                                it.points
                            }

                        val weekAgo =
                            System.currentTimeMillis() -
                                    (7 * 24 * 60 * 60 * 1000L)

                        val weeklyPoints =
                            histories
                                .filter {
                                    it.completedAt >= weekAgo
                                }
                                .sumOf {
                                    it.points
                                }

                        onResult(
                            ProfileStats(
                                totalPoints = totalPoints,
                                weeklyPoints = weeklyPoints
                            )
                        )
                    }
            }
    }
}


