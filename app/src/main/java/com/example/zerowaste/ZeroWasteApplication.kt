package com.example.zerowaste

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.zerowaste.notifications.ExpiryNotificationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class ZeroWasteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // جدولة الإشعار اليومي
        scheduleDailyExpiryCheck()
    }

    private fun scheduleDailyExpiryCheck() {
        // تحديد الساعة 9 صباحاً لليوم التالي
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9) // الساعة 9 صباحاً
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            // إذا كان الوقت الحالي قد تجاوز 9 صباحاً، نحدد الجدولة لليوم التالي
            if (before(currentTime)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        // حساب الفارق الزمني (المتبقي) بالمللي ثانية
        val delayMillis = targetTime.timeInMillis - currentTime.timeInMillis

        // بناء طلب العمل (WorkRequest) يتكرر كل 24 ساعة
        val workRequest = PeriodicWorkRequestBuilder<ExpiryNotificationWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS) // يبدأ بعد 9 صباحاً
            .build()

        // تسجيل العمل في WorkManager مع منع تكراره إذا تم جدولته مسبقاً
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_expiry_check", // اسم فريد للعمل
            ExistingPeriodicWorkPolicy.KEEP, // احتفظ بالجدولة القديمة إذا كانت موجودة
            workRequest
        )
    }
}