package com.pptv.httpsproject.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.pptv.httpsproject.R

class NotificationHelper constructor(private var mContext: Context) {
    private var manager: NotificationManager? = null

    companion object {
        private const val CHANNEL_ID = "channel_update"
        private const val NOTIFICATION_ID = 0
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, "应用更新", NotificationManager.IMPORTANCE_NONE)
            channel.description = "应用有新版本"
            channel.enableLights(true)//是否在桌面icon右上角展示小红点
            channel.setShowBadge(true)//徽章
            getManager().createNotificationChannel(channel)
        }
    }

    fun showNotification(content: String, apkUrl: String) {
        val intent: Intent = Intent(mContext, this::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl)
        val pendingIntent = PendingIntent.getService(
            mContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder: NotificationCompat.Builder = getNotify(content).setContentIntent(pendingIntent)
        getManager().notify(NOTIFICATION_ID, builder.build())
    }

    fun updateProgress(progress: Int) {
        val text = "下载中... $progress%"
        val intent: PendingIntent = PendingIntent.getActivity(mContext, 0, Intent(), PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = getNotify(text).setProgress(100, progress, false).setContentIntent(intent)
        getManager().notify(NOTIFICATION_ID, builder.build())
    }

    fun cancel() {
        getManager().cancel(NOTIFICATION_ID)
    }

    private fun getNotify(text: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(mContext.applicationContext, CHANNEL_ID)
            .setTicker("wwwwwww")
            .setContentTitle("版本更新")
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setWhen(System.currentTimeMillis())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    fun getManager(): NotificationManager {
        return if (manager == null) {
            manager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager!!
        } else {
            manager!!
        }
    }
}