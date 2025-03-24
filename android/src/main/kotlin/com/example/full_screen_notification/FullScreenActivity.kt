package com.example.full_screen_notification

import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.full_screen_notification.databinding.ActivityFullScreenBinding
import io.flutter.plugin.common.MethodChannel
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.FlutterEngine


class FullScreenActivity : AppCompatActivity() {
    private val CHANNEL = "com.example.my_channel"
    private lateinit var flutterEngine: FlutterEngine
    private lateinit var binding: ActivityFullScreenBinding
    private var mediaPlayer: MediaPlayer? = null
    private var isFirstResume = true
    private var openedFromLockScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cancelNotification()

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(screenStateReceiver, intentFilter)

        flutterEngine = FlutterEngine(this)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        binding = ActivityFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shouldPlayRingtone = intent?.getBooleanExtra("autoPlayRingtone", false) == true

        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        openedFromLockScreen = keyguardManager.isKeyguardLocked && shouldPlayRingtone

        if (openedFromLockScreen) {
            playRingtone()
        }

        val items = listOf("Main CR", "Option 1", "Option 2", "Option 3","Main CR", "Option 1", "Option 2", "Option 3","Main CR", "Option 1", "Option 2", "Option 3","Main CR", "Option 1", "Option 2", "Option 3","Main CR", "Option 1", "Option 2", "Option 3","Main CR", "Option 1", "Option 2", "Option 3","Main CR", "Option 1", "Option 2", "Option 3","Main CR", "Option 1", "Option 2", "Option 3","Main CR", "Option 1", "Option 2", "Option 3")
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        val extras = intent.extras
        if (extras != null) {

            val userName = extras.getString("userName", "Unknown Visitor")
            val visitorName = extras.getString("visitorName", "Unknown Visitor")
            val image = extras.getString("image", "Unknown Visitor")
            val logo = extras.getString("logo", "Unknown Visitor")
            val from = extras.getString("from", "Unknown Visitor")
            val purpose = extras.getString("purpose", "Unknown Visitor")
            val comments = extras.getString("comments", "Unknown Visitor")
            val email = extras.getString("email", "Unknown Visitor")


            binding.tvVisitorName.text = visitorName
            binding.tvEmail.text = email
            binding.tvCurrentUserName.text = userName
            binding.tvFrom.text = from
            binding.tvPurpose.text = purpose
            binding.tvComments.text = comments
            Glide.with(this)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_default_avatar)
                .into(binding.ivAvatar)

            Glide.with(this)
                .load(logo)
                .centerCrop()
                .into(binding.ivLogo)

            binding.spnMeetingAt.adapter = adapter

            binding.ivAcceptCall.setChannelClickListener(
                methodChannel,
                "acceptButtonClick",
                { binding.spnMeetingAt.selectedItem?.toString() ?: "" }
            )
            binding.ivDeclineCall.setChannelClickListener(methodChannel, "rejectButtonClick")
            binding.btnIgnoreButton.setChannelClickListener(methodChannel, "ignoreButtonClick")
            binding.btnAcceptClockOutButton.setChannelClickListener(
                methodChannel,
                "acceptAndClockOutClick",
                { binding.spnMeetingAt.selectedItem?.toString() ?: "" }
            )
        }

        Handler(Looper.getMainLooper()).postDelayed({
            methodChannel.invokeMethod("timeoutCallback", "Activity timed out")
            finish()
        }, 10000)
    }

    private fun playRingtone() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.notification_sound)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }
    }

    private fun stopRingtone() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
    }

    override fun onDestroy() {
        unregisterReceiver(screenStateReceiver)
        stopRingtone()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

        if (!isFirstResume) {
            stopRingtone()
        }
        isFirstResume = false

        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardLocked) {
            stopRingtone()
        }
    }

    private fun LinearLayout.setChannelClickListener(
        methodChannel: MethodChannel,
        methodName: String,
        getMeetAt: (() -> String)? = null
    ) {
        setOnClickListener {
            stopRingtone()

            val meetAt = getMeetAt?.invoke()
            methodChannel.invokeMethod(methodName, meetAt)
             finish()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        cancelNotification()
        stopRingtone()
    }

    private fun cancelNotification(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
    }

    private val screenStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    stopRingtone()
                }
                Intent.ACTION_SCREEN_ON -> {}
                Intent.ACTION_USER_PRESENT -> {
                    stopRingtone()
                    openedFromLockScreen = false
                }
            }
        }
    }
}