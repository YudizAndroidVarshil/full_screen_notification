package com.example.full_screen_notification

import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.example.full_screen_notification.utils.BundleHelper
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.Serializable

class FullScreenNotificationPlugin: FlutterPlugin, MethodCallHandler {
    private lateinit var channel : MethodChannel
    private lateinit var context: Context
    private lateinit var notificationHelper: NotificationHelper

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "full_screen_notification")
        context = flutterPluginBinding.applicationContext
        notificationHelper = NotificationHelper(context)
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "showFullScreenNotification" -> {
                val detail = call.arguments as? Map<String, Any>
                if (detail != null) {
                    showFullScreenNotification(detail)
                    result.success("Notification shown successfully")
                } else {
                    result.error("INVALID_ARGUMENT", "Notification details are required", null)
                }
            }
            else -> result.notImplemented()
        }
    }

    private fun showFullScreenNotification(detail: Map<String, Any>) {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val isDeviceLocked = keyguardManager.isKeyguardLocked

        val baseBundle = with(BundleHelper) { detail.toBundle() }

        val extrasForFullScreen = Bundle().apply {
            putAll(baseBundle)
            putBoolean("autoPlayRingtone", isDeviceLocked)
        }

        val extrasForAction = Bundle().apply {
            putAll(baseBundle)
            putBoolean("autoPlayRingtone", false)
        }

        val fullScreenIntent = Intent(context, FullScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtras(extrasForFullScreen)
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 88,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val actionIntent = Intent(context, FullScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtras(extrasForAction)
        }

        val actionPendingIntent = PendingIntent.getActivity(
            context, 1,
            actionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = Uri.parse("android.resource://${context.packageName}/${R.raw.notification_sound}")

        val notificationBuilder = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Incoming Call")
            .setContentText("${detail["name"] ?: "Someone"} is calling...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setSound(soundUri)
            .addAction(
                android.R.drawable.ic_menu_call,
                "View request",
                actionPendingIntent
            )
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
