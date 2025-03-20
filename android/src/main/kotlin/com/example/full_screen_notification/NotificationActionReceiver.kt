package com.example.full_screen_notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("FullScreenNotification", "NotificationActionReceiver - onReceive started")

        // Cancel the notification with the same ID used when posting it.
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)

        // Get the name from the intent
        val name = intent.getStringExtra("name")
        Log.d("FullScreenNotification", "Received name: $name")

        // Launch the full-screen activity.
        try {
            val fullScreenIntent = Intent(context, FullScreenActivity::class.java).apply {
                // Fix: Use separate statements for each flag
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra("name", name)
            }
            Log.d("FullScreenNotification", "Starting FullScreenActivity...")
            context.startActivity(fullScreenIntent)
            Log.d("FullScreenNotification", "FullScreenActivity intent sent")
        } catch (e: Exception) {
            Log.e("FullScreenNotification", "Error starting FullScreenActivity: ${e.message}", e)
        }
//        context.stopService(FullScreenActivity.serviceIntent);
    }
}