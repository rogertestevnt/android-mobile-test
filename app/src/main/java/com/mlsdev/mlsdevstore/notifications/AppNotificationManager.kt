package com.mlsdev.mlsdevstore.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.model.category.CategoryTreeNode
import com.mlsdev.mlsdevstore.presentaion.AppActivity
import com.mlsdev.mlsdevstore.presentaion.products.ProductsFragmentArgs

class AppNotificationManager(val applicationContext: Context) {
    private val channelId = "reminder_channel_id"

    fun sendNotification(category: CategoryTreeNode) {
        createNotificationChannel()
        val notificationId = 2019

        val argsBuilder = ProductsFragmentArgs.Builder()
        argsBuilder.categoryId = category.category.categoryId
        argsBuilder.categoryName = category.category.categoryName


        val uri = Uri.parse("https://mlsdevstore.com/products/" +
                "${category.category.categoryId}/${category.category.categoryName}")
        val implicitIntent = Intent(applicationContext, AppActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            data = uri
        }

        val implicitPendingIntent = PendingIntent.getActivity(applicationContext, 0, implicitIntent, 0)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.ic_mlsdev_logo)
                .setContentTitle("MLSDev Store")
                .setContentText("Check new products in the ${category.category.categoryName}")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(implicitPendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, notificationBuilder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Channel Name"
            val channelDescription = "Channel description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = channelDescription
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}