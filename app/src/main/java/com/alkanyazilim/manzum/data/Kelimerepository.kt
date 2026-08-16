package com.alkanyazilim.manzum.data

import kotlinx.coroutines.flow.Flow

class KelimeRepository(private val kelimeDao: KelimeDao) {
    fun tumBilinenIdler(): Flow<List<Int>> = kelimeDao.tumBilinenIdler()

    suspend fun bilinenDurumunuDegistir(kelimeId: Int, suAndaBiliniyorMu: Boolean) {
        if (suAndaBiliniyorMu) {
            kelimeDao.bilinmeyenYap(BilinenKelime(kelimeId))
        } else {
            kelimeDao.bilinenYap(BilinenKelime(kelimeId))
        }
    }
}