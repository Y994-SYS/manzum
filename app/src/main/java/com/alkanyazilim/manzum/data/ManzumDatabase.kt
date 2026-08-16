package com.alkanyazilim.manzum.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriBeyit::class, BilinenKelime::class],
    version = 2,
    exportSchema = false
)
abstract class ManzumDatabase : RoomDatabase() {
    abstract fun favoriDao(): FavoriDao
    abstract fun kelimeDao(): KelimeDao

    companion object {
        @Volatile private var INSTANCE: ManzumDatabase? = null

        fun getInstance(context: Context): ManzumDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ManzumDatabase::class.java,
                    "manzum_db"
                )
                    // Henüz yayınlanmamış / erken aşama uygulama olduğu için basit yol:
                    // gerçek kullanıcı verisi biriktikten sonra bunun yerine gerçek
                    // bir Migration(1, 2) yazılması önerilir.
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}