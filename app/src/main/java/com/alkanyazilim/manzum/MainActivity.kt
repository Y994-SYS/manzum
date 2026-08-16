package com.alkanyazilim.manzum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.alkanyazilim.manzum.data.BeyitRepository
import com.alkanyazilim.manzum.data.FavoriRepository
import com.alkanyazilim.manzum.data.KelimeRepository
import com.alkanyazilim.manzum.data.ManzumDatabase
import com.alkanyazilim.manzum.data.loadGazeller
import com.alkanyazilim.manzum.data.loadKelimeKartlari
import com.alkanyazilim.manzum.ui.*
import com.alkanyazilim.manzum.ui.theme.ManzumTheme
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {

    private val bildirimIzniLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Native (Android 12+) SplashScreen API — ilk kare bu, res/values/themes.xml'deki
        // Theme.Manzum.Starting temasından arka plan rengini alır.
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            bildirimIzniLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        com.alkanyazilim.manzum.notification.gunlukBildirimiZamanla(applicationContext)

        // Bildirime tıklanınca gelen beyit id'si (varsa)
        val bildirimdenGelenBeyitId = intent?.getIntExtra("beyitId", -1)
            ?.takeIf { it != -1 }

        val repository = BeyitRepository(applicationContext)
        val db = ManzumDatabase.getInstance(applicationContext)
        val favoriRepository = FavoriRepository(db.favoriDao())
        val kelimeRepository = KelimeRepository(db.kelimeDao())

        setContent {
            ManzumTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Native splash kapandıktan hemen sonra kısa süreli markalı geçiş ekranı
                    var splashBitti by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(700)
                        splashBitti = true
                    }

                    if (!splashBitti) {
                        ManzumSplashEkrani()
                    } else {
                        val kelimeler = remember { loadKelimeKartlari(applicationContext) }
                        val viewModel: ManzumViewModel = viewModel(
                            factory = ManzumViewModelFactory(repository, favoriRepository, kelimeRepository, kelimeler)
                        )
                        val navController = rememberNavController()
                        val tumBeyitler by viewModel.beyitler.collectAsState()
                        val tumGazeller = remember { loadGazeller(applicationContext) }

                        // Bildirimden açıldıysa, beyitler yüklenir yüklenmez detay ekranına atla
                        LaunchedEffect(bildirimdenGelenBeyitId, tumBeyitler) {
                            if (bildirimdenGelenBeyitId != null && tumBeyitler.isNotEmpty()) {
                                navController.navigate("detay/$bildirimdenGelenBeyitId") {
                                    popUpTo("mood")
                                }
                            }
                        }

                        NavHost(navController, startDestination = "mood") {
                            composable("mood") {
                                MoodSelectorScreen(
                                    onKategoriSecildi = { kategori ->
                                        val beyit = viewModel.kategoriyeGoreBeyit(kategori)
                                        if (beyit != null) navController.navigate("detay/${beyit.id}")
                                    },
                                    onFavorilerTikla = { navController.navigate("favoriler") },
                                    onSairlerTikla = { navController.navigate("sairler") },
                                    onKelimeKartlariTikla = { navController.navigate("kelimeKartlari") }
                                )
                            }
                            composable("favoriler") {
                                val favoriler by viewModel.favoriBeyitlerAkisi.collectAsState()
                                FavoritesScreen(
                                    favoriler = favoriler,
                                    onBeyitTikla = { beyit -> navController.navigate("detay/${beyit.id}") }
                                )
                            }
                            composable("sairler") {
                                PoetsScreen(
                                    tumBeyitler = tumBeyitler,
                                    onSairTikla = { sair ->
                                        val kodlanmisSair = URLEncoder.encode(sair, "UTF-8")
                                        navController.navigate("sairDetay/$kodlanmisSair")
                                    }
                                )
                            }
                            composable(
                                "sairDetay/{sair}",
                                arguments = listOf(navArgument("sair") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val kodlanmisSair = backStackEntry.arguments?.getString("sair") ?: ""
                                val sair = URLDecoder.decode(kodlanmisSair, "UTF-8")
                                val sairinBeyitleri = tumBeyitler.filter { it.sair == sair }
                                PoetDetailScreen(
                                    sair = sair,
                                    sairinBeyitleri = sairinBeyitleri,
                                    tumGazeller = tumGazeller
                                )
                            }
                            composable(
                                "detay/{beyitId}",
                                arguments = listOf(navArgument("beyitId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val id = backStackEntry.arguments?.getInt("beyitId") ?: -1
                                val beyit = viewModel.beyitBul(id)
                                val favoriIdler by viewModel.favoriIdler.collectAsState()
                                PoemDetailScreen(
                                    beyit = beyit,
                                    favoriMi = favoriIdler.contains(id),
                                    onFavoriTikla = { viewModel.favoriDegistir(id) },
                                    onPaylasTikla = {
                                        if (beyit != null) {
                                            com.alkanyazilim.manzum.util.beyitiPaylas(navController.context, beyit)
                                        }
                                    }
                                )
                            }
                            composable("kelimeKartlari") {
                                val guncelKart by viewModel.guncelKelimeKarti.collectAsState()
                                val bilinenIdler by viewModel.bilinenKelimeIdler.collectAsState()
                                val (_, toplamSayisi) = viewModel.kelimeIlerlemesi()
                                KelimeKartlariScreen(
                                    guncelKart = guncelKart,
                                    bilinenSayisi = bilinenIdler.size,
                                    toplamSayisi = toplamSayisi,
                                    kategoriIlerlemesi = viewModel.kategoriBazliKelimeIlerlemesi(),
                                    onBiliyorum = { viewModel.kelimeyiBiliyorum() },
                                    onTekrarGoster = { viewModel.kelimeyiTekrarGoster() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}