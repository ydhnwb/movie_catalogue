package com.ydhnwb.moviecatalogue.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.app.AlarmManager
import android.os.Build
import android.app.NotificationManager
import android.app.NotificationChannel
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import android.media.RingtoneManager
import com.ydhnwb.moviecatalogue.MainActivity
import com.ydhnwb.moviecatalogue.R
import java.util.*


class DailyReminderReceiver : BroadcastReceiver(){

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "123"
        const val NOTIFICATION_ID = 1234
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.let {
            sendNotification(it, it.getString(R.string.app_name), it.getString(R.string.notification_message_daily), NOTIFICATION_ID)
        }
    }

    private fun sendNotification(context: Context, title: String, desc: String, id: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = PendingIntent.getActivity(
            context, id, Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_movie_black_24dp)
            setContentTitle(title)
            setContentText(desc)
            setContentIntent(pendingIntent)
            color = ContextCompat.getColor(context, android.R.color.transparent)
            setVibrate(longArrayOf(500, 100, 300, 300, 400))
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(true)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "NOTIFICATION_DAILY",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.YELLOW
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            }
            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(id, builder.build())

    }

    fun setAlarm(context: Context) {
        cancelAlarm(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().also {
            it.set(Calendar.HOUR_OF_DAY, 7)
            it.set(Calendar.MINUTE, 0)
            it.set(Calendar.SECOND, 0)
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, getPendingIntent(context))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, getPendingIntent(context))
        }
    }

    private fun getPendingIntent(context: Context): PendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, Intent(context, DailyReminderReceiver::class.java), PendingIntent.FLAG_CANCEL_CURRENT)

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(getPendingIntent(context))
    }

}