package com.example.offerkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.offerkotlin.ui.screen.OfferFormScreen
import com.example.offerkotlin.ui.screen.OfferScreen
import com.example.offerkotlin.ui.theme.OfferKotlinTheme
import com.example.offerkotlin.viewmodel.OfferViewModel

// Centralizamos las rutas para evitar errores
object Routes {
    const val OFFER_LIST = "offer_list"
    const val OFFER_FORM = "offer_form/{offerId}"
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
                        startDestination = Routes.OFFER_LIST // Inicia en la pantalla con filtros
                    ) {
                        // Pantalla principal con filtros, búsqueda y lista
                        composable(Routes.OFFER_LIST) {
                            OfferScreen(viewModel = offerViewModel)
                        }

                        // Pantalla de creación o edición
                        composable(
                            route = Routes.OFFER_FORM,
                            arguments = listOf(
                                navArgument("offerId") {
                                    type = NavType.IntType
                                    defaultValue = -1 // -1 significa que se va a crear una nueva oferta
                                }
                            )
                        ) { backStackEntry ->
                            val offerId = backStackEntry.arguments?.getInt("offerId") ?: -1
                            OfferFormScreen(
                                navController = navController,
                                viewModel = offerViewModel,
                                offerId = offerId
                            )
                        }
                    }
                }
            }
        }
    }
}
