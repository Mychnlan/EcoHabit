package com.team4.ecohabit.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.team4.ecohabit.receiver.HabitReminderReceiver
import java.util.Calendar

object ReminderScheduler {

    fun scheduleReminder(
        context: Context,
        habitName: String,
        hour: Int,
        minute: Int
    ) {

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        val intent = Intent(
            context,
            HabitReminderReceiver::class.java
        ).apply {

            putExtra(
                "habit_name",
                habitName
            )
        }

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                habitName.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val calendar = Calendar.getInstance().apply {

            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)

            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}