package com.alkanyazilim.manzum.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alkanyazilim.manzum.MainActivity
import com.alkanyazilim.manzum.data.BeyitRepository
import com.alkanyazilim.manzum.util.olusturBeyitKarti

const val BILDIRIM_KANAL_ID = "manzum_gunluk_beyit"
const val BILDIRIM_ID = 1001

class BeyitBildirimWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val repository = BeyitRepository(applicationContext)
        val beyit = repository.tumBeyitler().randomOrNull() ?: return Result.success()

        bildirimKanaliOlustur()

        val kart = olusturBeyitKarti(applicationContext, beyit)

        val intent = android.content.Intent(applicationContext, MainActivity::class.java).apply {
            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            // data alanı her beyit için farklı olsun ki PendingIntent'ler
            // birbirinin üzerine yazılmasın (aynı requestCode/action ile
            // eski günün beyitine giden intent önbellekte kalmasın)
            data = Uri.parse("manzum://detay/${beyit.id}")
            putExtra("beyitId", beyit.id)
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, beyit.id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val ilkSatir = beyit.orijinalMetin.lineSequence().first()

        val bildirim = NotificationCompat.Builder(applicationContext, BILDIRIM_KANAL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_edit)
            .setContentTitle(beyit.sair)
            .setContentText(ilkSatir)
            .setLargeIcon(kart)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(kart)
                    .bigLargeIcon(null as android.graphics.Bitmap?)
                    .setSummaryText(ilkSatir)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                applicationContext, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            androidx.core.app.NotificationManagerCompat.from(applicationContext)
                .notify(BILDIRIM_ID, bildirim)
        }

        return Result.success()
    }

    private fun bildirimKanaliOlustur() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val kanal = NotificationChannel(
                BILDIRIM_KANAL_ID,
                "Günlük Beyit",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Her sabah yeni bir beyit hatırlatması"
            }
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(kanal)
        }
    }
}