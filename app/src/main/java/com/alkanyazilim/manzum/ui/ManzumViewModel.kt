package com.alkanyazilim.manzum.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alkanyazilim.manzum.data.Beyit
import com.alkanyazilim.manzum.data.BeyitRepository
import com.alkanyazilim.manzum.data.FavoriRepository
import com.alkanyazilim.manzum.data.KelimeKarti
import com.alkanyazilim.manzum.data.KelimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManzumViewModel(
    private val repository: BeyitRepository,
    private val favoriRepository: FavoriRepository,
    private val kelimeRepository: KelimeRepository,
    private val kelimeler: List<KelimeKarti>
) : ViewModel() {
    private val _beyitler = MutableStateFlow<List<Beyit>>(emptyList())
    val beyitler: StateFlow<List<Beyit>> = _beyitler

    val favoriIdler: StateFlow<List<Int>> = favoriRepository.tumFavoriIdler()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ---- Kelime Kartlarım (init bloğundan ÖNCE tanımlanmalı, çünkü init içinde kullanılıyor) ----

    val bilinenKelimeIdler: StateFlow<List<Int>> = kelimeRepository.tumBilinenIdler()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val sonGosterilenKelimeIdler = mutableListOf<Int>()

    private val _guncelKelimeKarti = MutableStateFlow<KelimeKarti?>(null)
    val guncelKelimeKarti: StateFlow<KelimeKarti?> = _guncelKelimeKarti

    init {
        viewModelScope.launch { _beyitler.value = repository.tumBeyitler() }
        sonrakiKelimeKarti()
    }

    fun kategoriyeGoreBeyit(kategori: String): Beyit? =
        beyitler.value.filter { it.kategori == kategori }.randomOrNull()

    fun beyitBul(id: Int): Beyit? = beyitler.value.find { it.id == id }

    fun favoriDegistir(beyitId: Int) {
        viewModelScope.launch {
            val suAndaFavoriMi = favoriIdler.value.contains(beyitId)
            favoriRepository.favoriDurumunuDegistir(beyitId, suAndaFavoriMi)
        }
    }

    val favoriBeyitlerAkisi: StateFlow<List<Beyit>> =
        kotlinx.coroutines.flow.combine(beyitler, favoriIdler) { tumBeyitler, idler ->
            tumBeyitler.filter { idler.contains(it.id) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun favoriBeyitler(): List<Beyit> =
        beyitler.value.filter { favoriIdler.value.contains(it.id) }

    fun sonrakiKelimeKarti() {
        if (kelimeler.isEmpty()) {
            _guncelKelimeKarti.value = null
            return
        }
        val bilinenler = bilinenKelimeIdler.value.toSet()
        var adaylar = kelimeler.filter { it.id !in bilinenler && it.id !in sonGosterilenKelimeIdler }
        if (adaylar.isEmpty()) {
            // son gösterilenler kısıtını gevşet, sadece bilinenleri hariç tut
            adaylar = kelimeler.filter { it.id !in bilinenler }
        }
        if (adaylar.isEmpty()) {
            // tüm kelimeler biliniyor
            _guncelKelimeKarti.value = null
            return
        }
        val secilen = adaylar.random()
        _guncelKelimeKarti.value = secilen
        sonGosterilenKelimeIdler.add(secilen.id)
        if (sonGosterilenKelimeIdler.size > 5) sonGosterilenKelimeIdler.removeAt(0)
    }

    fun kelimeyiBiliyorum() {
        val kart = _guncelKelimeKarti.value ?: return
        viewModelScope.launch {
            kelimeRepository.bilinenDurumunuDegistir(kart.id, suAndaBiliniyorMu = false)
            sonrakiKelimeKarti()
        }
    }

    fun kelimeyiTekrarGoster() {
        sonrakiKelimeKarti()
    }

    fun kelimeIlerlemesi(): Pair<Int, Int> =
        bilinenKelimeIdler.value.size to kelimeler.size

    data class KategoriIlerlemesi(val kategori: String, val bilinen: Int, val toplam: Int)

    fun kategoriBazliKelimeIlerlemesi(): List<KategoriIlerlemesi> {
        val bilinenler = bilinenKelimeIdler.value.toSet()
        return kelimeler.groupBy { it.kategori }
            .map { (kategori, liste) ->
                KategoriIlerlemesi(kategori, liste.count { it.id in bilinenler }, liste.size)
            }
            .sortedByDescending { it.toplam }
    }
}

class ManzumViewModelFactory(
    private val repository: BeyitRepository,
    private val favoriRepository: FavoriRepository,
    private val kelimeRepository: KelimeRepository,
    private val kelimeler: List<KelimeKarti>
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ManzumViewModel(repository, favoriRepository, kelimeRepository, kelimeler) as T
    }
}