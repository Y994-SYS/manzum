package com.alkanyazilim.manzum.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@androidx.room.Entity(tableName = "favoriler")
data class FavoriBeyit(
    @androidx.room.PrimaryKey val beyitId: Int
)

@Dao
interface FavoriDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ekle(favori: FavoriBeyit)

    @Delete
    suspend fun sil(favori: FavoriBeyit)

    @Query("SELECT beyitId FROM favoriler")
    fun tumFavoriIdler(): Flow<List<Int>>

    @Query("SELECT EXISTS(SELECT 1 FROM favoriler WHERE beyitId = :beyitId)")
    suspend fun favoriMi(beyitId: Int): Boolean
}