package com.example.northamptontravels.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.northamptontravels.R

//todo send notification once post reply
class ReplyActivity : AppCompatActivity() {

    val TheChannelID = "ChannelID"
    val TheChannelName = "ChannelName"
    val TheNotificationID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)

        createTheNotificationChannel()

        val builder = NotificationCompat.Builder(this, TheChannelID)
            .setContentTitle("Sample Title")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("This is the message body")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notifManager = NotificationManagerCompat.from(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
//    notifManager.notify(TheNotificationID, builder)

    }

    fun createTheNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = TheChannelName
            val descriptionText = "moving On"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(TheChannelID, name, importance).apply {
                description = descriptionText
            }

            //register channel with system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}