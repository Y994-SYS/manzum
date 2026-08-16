package com.alkanyazilim.manzum.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@androidx.room.Entity(tableName = "bilinen_kelimeler")
data class BilinenKelime(
    @androidx.room.PrimaryKey val kelimeId: Int
)

@Dao
interface KelimeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun bilinenYap(kelime: BilinenKelime)

    @Delete
    suspend fun bilinmeyenYap(kelime: BilinenKelime)

    @Query("SELECT kelimeId FROM bilinen_kelimeler")
    fun tumBilinenIdler(): Flow<List<Int>>

    @Query("SELECT EXISTS(SELECT 1 FROM bilinen_kelimeler WHERE kelimeId = :kelimeId)")
    suspend fun biliniyorMu(kelimeId: Int): Boolean
}