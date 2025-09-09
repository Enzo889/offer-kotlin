package com.example.offerkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.offerkotlin.ui.screen.OfferDetailScreen
import com.example.offerkotlin.ui.screen.OfferScreen
import com.example.offerkotlin.ui.theme.OfferKotlinTheme
import com.example.offerkotlin.viewmodel.OfferViewModel
import androidx.compose.runtime.collectAsState

// Centralizamos las rutas para evitar errores
object Routes {
    const val OFFER_LIST = "offer_list"
    const val OFFER_DETAIL = "offer_detail/{offerId}"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OfferKotlinTheme {
                val navController = rememberNavController()
                val offerViewModel: OfferViewModel = viewModel()

                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.OFFER_LIST
                    ) {
                        // Pantalla principal
                        composable(Routes.OFFER_LIST) {
                            OfferScreen(
                                viewModel = offerViewModel,
                                navController = navController // <-- Pasamos navController
                            )
                        }

                        // pantalla de detalles
                        composable(
                            route = Routes.OFFER_DETAIL,
                            arguments = listOf(navArgument("offerId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val offerId = backStackEntry.arguments?.getInt("offerId")
                            val offer = offerViewModel.offers.collectAsState().value.find { it.id == offerId }

                            if (offer != null) {
                                OfferDetailScreen(offer = offer)
                            } else {
                                Text("Oferta no encontrada", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}
