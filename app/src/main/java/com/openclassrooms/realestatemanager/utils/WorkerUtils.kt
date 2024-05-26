package com.openclassrooms.realestatemanager.utils

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.openclassrooms.realestatemanager.worker.EstateNotificationWorker

class WorkerUtils {

    companion object {
        fun scheduleEstateNotification(context: Context) {
            val workRequest = OneTimeWorkRequest.Builder(EstateNotificationWorker::class.java)
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}