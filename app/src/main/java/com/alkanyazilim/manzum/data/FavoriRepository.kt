package com.alkanyazilim.manzum.data

import kotlinx.coroutines.flow.Flow

class FavoriRepository(private val dao: FavoriDao) {
    fun tumFavoriIdler(): Flow<List<Int>> = dao.tumFavoriIdler()

    suspend fun favoriDurumunuDegistir(beyitId: Int, suAndaFavoriMi: Boolean) {
        if (suAndaFavoriMi) dao.sil(FavoriBeyit(beyitId))
        else dao.ekle(FavoriBeyit(beyitId))
    }
}