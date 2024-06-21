package com.openclassrooms.realestatemanager.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.openclassrooms.realestatemanager.R

/**
 * A Worker that creates a notification when an estate is added
 *
 * @param context The context
 * @param workerParams The parameters for the Worker
 */
class EstateNotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    /**
     * The main method of the Worker that is executed in the background
     *
     * @return The result of the work
     */
    override fun doWork(): Result {

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ESTATE_CHANNEL_ID,
                applicationContext.getString(R.string.notification_estate_added),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = applicationContext.getString(R.string.notification_estate_added) }
            notificationManager.createNotificationChannel(channel)
        }

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, ESTATE_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_location_pin_24)
                .setContentTitle(applicationContext.getString(R.string.notification_estate_added))
                .setContentText(applicationContext.getString(R.string.notification_estate_added_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.failure()
        }

        notificationManager.notify(ESTATE_NOTIFICATION_ID, builder.build())
        return Result.success()
    }

    companion object {
        const val ESTATE_ID_KEY = "estate_id"
        const val UNIQUE_WORK_NAME = "estate_added_notification"
        private const val ESTATE_CHANNEL_ID = "estate_channel"
        private const val ESTATE_NOTIFICATION_ID = 1
    }
}