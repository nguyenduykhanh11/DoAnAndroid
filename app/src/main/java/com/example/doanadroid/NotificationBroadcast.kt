package com.example.doanadroid

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

const val notifiExtra = "notifiExtra"
const val chanelID = "chanel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
class NotificationBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.dai_dien)
        val notification = NotificationCompat.Builder(context!!, chanelID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(intent?.getStringExtra(titleExtra))
            .setContentText(intent?.getStringExtra(messageExtra))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setColor(ContextCompat.getColor(context, R.color.bg_item_list_data))
            .setLargeIcon(bitmap)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = intent?.getIntExtra(notifiExtra, 0)
        manager.notify(id!!,notification.build())
    }
}