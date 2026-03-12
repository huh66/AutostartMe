package com.example.autostartme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val prefs = AutostartPreferences(context)
        val apps = prefs.getApps()
        if (apps.isEmpty()) return

        if (!Settings.canDrawOverlays(context)) {
            showPermissionNotification(context)
            return
        }

        val pendingResult = goAsync()
        Thread {
            try {
                val pm = context.packageManager
                for (packageName in apps) {
                    try {
                        val launchIntent = pm.getLaunchIntentForPackage(packageName) ?: continue
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(launchIntent)
                        Log.i(TAG, "Launched: $packageName")
                        Thread.sleep(LAUNCH_DELAY_MS)
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to launch: $packageName", e)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }.start()
    }

    private fun showPermissionNotification(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        nm.createNotificationChannel(channel)

        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, openIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(context.getString(R.string.notification_permission_title))
            .setContentText(context.getString(R.string.notification_permission_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        nm.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val TAG = "BootReceiver"
        private const val CHANNEL_ID = "autostart_channel"
        private const val NOTIFICATION_ID = 1001
        private const val LAUNCH_DELAY_MS = 1500L
    }
}
