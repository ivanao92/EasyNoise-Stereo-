package com.cliambrown.easynoise

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cliambrown.easynoise.helpers.*

class NotificationUtils(base: Context?) : ContextWrapper(base) {

    private var mManager: NotificationManager? = null

    companion object {
        var NOTIFICATION_ID = 64 // Random number
    }

    private fun getManager(): NotificationManager? {
        if (mManager == null) {
            mManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        return mManager
    }

    fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                enableLights(false)
                enableVibration(false)
            }
            // Register the channel with the system
            getManager()?.createNotificationChannel(channel)
        }
    }

    fun createNotification(): Notification {

        val playIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = PLAY
        }
        val playPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, playIntent, 0)

        val pauseIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = PAUSE
        }
        val pausePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, pauseIntent, 0)

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(R.mipmap.ic_launcher, getString(R.string.play), playPendingIntent)
            .addAction(R.mipmap.ic_launcher, getString(R.string.pause), pausePendingIntent)
            .build()

        return notification
    }
}