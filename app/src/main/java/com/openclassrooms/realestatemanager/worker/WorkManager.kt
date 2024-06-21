package com.openclassrooms.realestatemanager.worker

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

/**
 * Manages work tasks for the application
 *
 * @property appContext The context
 */
class WorkManager(private val appContext: Context) {

    /**
     * Schedules a work request to show a notification when an estate is added
     */
    fun scheduleEstateNotification() {
        val workRequest = OneTimeWorkRequest.Builder(EstateNotificationWorker::class.java)
            .build()
        WorkManager.getInstance(appContext).enqueue(workRequest)
    }
}