package com.example.full_screen_notification

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.full_screen_notification.databinding.ActivityFullScreenBinding
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.FlutterEngine


class FullScreenActivity : AppCompatActivity() {
    private val CHANNEL = "com.example.my_channel"
    private lateinit var flutterEngine: FlutterEngine
    private lateinit var binding: ActivityFullScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cancelNotification()

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        flutterEngine = FlutterEngine(this)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        binding = ActivityFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        if (extras != null) {

            val userName = extras.getString("userName", "Unknown Visitor")
            val visitorName = extras.getString("visitorName", "Unknown Visitor")
            val image = extras.getString("image", "Unknown Visitor")
            val from = extras.getString("from", "Unknown Visitor")
            val purpose = extras.getString("purpose", "Unknown Visitor")
            val comments = extras.getString("comments", "Unknown Visitor")


            binding.tvVisitorName.text = visitorName
            binding.tvCurrentUserName.text = userName
            binding.tvFrom.text = from
            binding.tvPurpose.text = purpose
            binding.tvComments.text = comments
            Glide.with(this)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_default_avatar)
                .into(binding.ivAvatar)

            val methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)

            binding.ivAcceptCall.setChannelClickListener(methodChannel, "acceptButtonClick")
            binding.ivDeclineCall.setChannelClickListener(methodChannel, "rejectButtonClick")
            binding.btnIgnoreButton.setChannelClickListener(methodChannel, "ignoreButtonClick")
            binding.btnAcceptClockOutButton.setChannelClickListener(methodChannel, "acceptAndClockOutClick")

        }
    }

    fun LinearLayout.setChannelClickListener(methodChannel: MethodChannel, methodName: String) {
        setOnClickListener {
            methodChannel.invokeMethod(methodName, null)
//             finish()
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
       cancelNotification()
    }

    private fun cancelNotification(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
    }
}