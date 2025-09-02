package com.example.offerkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfferKotlinTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: OfferViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "offerList"
    ) {
        // Pantalla de listado
        composable("offerList") {
            OfferScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // Pantalla de formulario para crear oferta
        composable("offerForm") {
            OfferFormScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // Pantalla de formulario para editar oferta (con argumento)
        composable(
            route = "offerForm/{offerId}",
            arguments = listOf(navArgument("offerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val offerId = backStackEntry.arguments?.getInt("offerId")
            OfferFormScreen(
                viewModel = viewModel,
                navController = navController,
                offerId = offerId
            )
        }
    }
}
