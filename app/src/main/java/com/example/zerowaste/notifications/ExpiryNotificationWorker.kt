package com.example.zerowaste.notifications

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.zerowaste.data.local.FoodItemDao
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FoodItemDaoEntryPoint {
    fun getFoodItemDao(): FoodItemDao
}

class ExpiryNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val daoEntryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            FoodItemDaoEntryPoint::class.java
        )
        val dao = daoEntryPoint.getFoodItemDao()

        val today = LocalDate.now()
        val threeDaysFromNow = today.plusDays(3)
        val expiringItems = dao.getExpiringItems(threeDaysFromNow).first()

        if (expiringItems.isNotEmpty()) {
            val notificationHelper = NotificationHelper(applicationContext)
            notificationHelper.createChannel()

            val itemsText = expiringItems.joinToString(separator = "، ") { it.name }
            val message = "لديك ${expiringItems.size} عناصر تنتهي صلاحيتها قريباً: $itemsText"

            notificationHelper.showExpiryNotification(message)
        }

        return Result.success()
    }
}