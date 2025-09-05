package com.example.offerkotlin.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.offerkotlin.viewmodel.OfferViewModel

@Composable
fun OfferListScreen(
    navController: NavHostController,
    viewModel: OfferViewModel
) {
    val offers by viewModel.offers.collectAsState()
    val message by viewModel.message.collectAsState()

    // Cargar datos al iniciar
    LaunchedEffect(Unit) {
        viewModel.fetchOffers()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Marketplace", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search products...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(offers) { offer ->
                OfferItem(
                    offer = offer,
                    onEdit = { navController.navigate("offer_form/${offer.id}") },
                    onDelete = { viewModel.deleteOffer(offer.id) }
                )
            }
        }

        message?.let {
            Snackbar {
                Text(it)
            }
        }
    }
}
