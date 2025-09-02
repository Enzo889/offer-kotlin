package com.example.offerkotlin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.offerkotlin.data.model.Offer
import com.example.offerkotlin.viewmodel.OfferViewModel

@Composable
fun OfferFormScreen(
    viewModel: OfferViewModel,
    navController: NavController,
    offerId: Int? = null
) {
    val offers by viewModel.offers.collectAsState()
    val currentOffer = offers.find { it.id == offerId }

    var name by remember { mutableStateOf(currentOffer?.name ?: "") }
    var price by remember { mutableStateOf(currentOffer?.price?.toString() ?: "") }
    var location by remember { mutableStateOf(currentOffer?.location ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            if (offerId == null) "Crear Nueva Oferta" else "Editar Oferta",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val offer = Offer(
                    id = currentOffer?.id,
                    name = name,
                    price = price.toDoubleOrNull() ?: 0.0,
                    location = location,
                    image = currentOffer?.image ?: "https://placehold.co/100x100",
                    stock = currentOffer?.stock ?: "Disponible",
                    discount = currentOffer?.discount ?: 0,
                    installments = currentOffer?.installments ?: "6x",
                    shipping = currentOffer?.shipping ?: "Gratis",
                    description = currentOffer?.description ?: "Sin descripción",
                    category = currentOffer?.category ?: "General",
                    categoryId = currentOffer?.categoryId ?: 0,
                    condition = currentOffer?.condition ?: "Nuevo",
                    seller = currentOffer?.seller ?: "Desconocido",
                    isOwner = true
                )

                if (offerId == null) {
                    viewModel.createOffer(offer)
                } else {
                    offer.id?.let { id -> viewModel.updateOffer(id, offer) }
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (offerId == null) "Crear" else "Actualizar")
        }
    }
}
