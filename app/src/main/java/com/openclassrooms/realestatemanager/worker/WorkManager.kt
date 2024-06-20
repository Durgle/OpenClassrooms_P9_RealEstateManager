package com.openclassrooms.realestatemanager.worker

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class WorkManager(private val appContext: Context) {

    fun scheduleEstateNotification() {
        val workRequest = OneTimeWorkRequest.Builder(EstateNotificationWorker::class.java)
            .build()
        WorkManager.getInstance(appContext).enqueue(workRequest)
    }
}