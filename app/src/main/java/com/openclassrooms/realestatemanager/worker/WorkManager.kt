package com.openclassrooms.realestatemanager.worker

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class WorkManager(appContext: Context) {

    val context: Context

    init {
        context = appContext
    }

    fun scheduleEstateNotification() {
        val workRequest = OneTimeWorkRequest.Builder(EstateNotificationWorker::class.java)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}