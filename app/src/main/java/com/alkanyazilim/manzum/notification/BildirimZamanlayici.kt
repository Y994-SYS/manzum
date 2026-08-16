package com.alkanyazilim.manzum.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun gunlukBildirimiZamanla(context: Context) {
    val simdi = Calendar.getInstance()
    val hedef = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        if (before(simdi)) add(Calendar.DAY_OF_YEAR, 1)
    }
    val ilkGecikme = hedef.timeInMillis - simdi.timeInMillis

    val istek = PeriodicWorkRequestBuilder<BeyitBildirimWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(ilkGecikme, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "gunluk_beyit_bildirimi",
        ExistingPeriodicWorkPolicy.KEEP,
        istek
    )
}