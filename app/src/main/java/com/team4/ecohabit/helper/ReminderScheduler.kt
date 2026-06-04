package com.team4.ecohabit.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.team4.ecohabit.receiver.HabitReminderReceiver
import java.util.Calendar

object ReminderScheduler {

    fun scheduleReminder(
        context: Context,
        habitName: String,
        hour: Int,
        minute: Int
    ) {
        Log.d(
            "REMINDER",
            "Scheduling $habitName at $hour:$minute"
        )


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

        val calendar = Calendar.getInstance()

        calendar.set(
            Calendar.HOUR_OF_DAY,
            hour
        )

        calendar.set(
            Calendar.MINUTE,
            minute
        )

        calendar.set(
            Calendar.SECOND,
            0
        )

        calendar.set(
            Calendar.MILLISECOND,
            0
        )

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(
                Calendar.DAY_OF_MONTH,
                1
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                !alarmManager.canScheduleExactAlarms()
            ) {

                val intent = Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                )

                context.startActivity(intent)
            }

            if (alarmManager.canScheduleExactAlarms()) {

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )

            } else {

                Log.e(
                    "REMINDER",
                    "Exact alarm permission denied"
                )
            }

        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d(
                "REMINDER",
                "Scheduling for ${calendar.time}"
            )
        }
    }

    fun cancelReminder(
        context: Context,
        habitName: String
    ) {

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        val intent = Intent(
            context,
            HabitReminderReceiver::class.java
        )

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                habitName.hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

        alarmManager.cancel(pendingIntent)

        pendingIntent.cancel()
    }
}

object ReminderPermissionHelper {

    fun hasExactAlarmPermission(
        context: Context
    ): Boolean {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return true
        }

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        return alarmManager.canScheduleExactAlarms()
    }

    fun requestExactAlarmPermission(
        context: Context
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            val intent = Intent(
                Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            )

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK

            context.startActivity(intent)
        }
    }
}