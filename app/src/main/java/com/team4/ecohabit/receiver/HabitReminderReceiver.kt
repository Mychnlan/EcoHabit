package com.team4.ecohabit.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.team4.ecohabit.R

class HabitReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val habitName =
            intent.getStringExtra("habit_name")
                ?: "Eco Habit"

        val notification =
            NotificationCompat.Builder(
                context,
                "habit_channel"
            )
                .setSmallIcon(R.drawable.ic_leaf)
                .setContentTitle("🌱 Eco Habit Reminder")
                .setContentText("Time for: $habitName")
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .build()

        val manager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        manager.notify(
            habitName.hashCode(),
            notification
        )
    }
}