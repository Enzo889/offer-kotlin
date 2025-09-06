package com.example.offerkotlin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.offerkotlin.data.model.Offer
import com.example.offerkotlin.viewmodel.OfferViewModel
@Composable
fun OfferFormScreen(
    navController: NavHostController,
    viewModel: OfferViewModel,
    offerId: Int
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(offerId) {
        if (offerId != -1) {
            val offer = viewModel.offers.value.find { it.id == offerId }
            offer?.let {
                name = it.name
                price = it.price.toString()
                description = it.description.toString()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (offerId == -1) "Create Offer" else "Edit Offer",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val newOffer = Offer(
                    id = offerId,
                    name = name,
                    image = "/default.jpg",
                    stock = "Available",
                    price = price.toDouble(), // Hardcodeado por ahora
                    discount = 0,
                    installments = "",
                    shipping = "",
                    description = description,
                    category = "",
                    categoryId = 0,
                    condition = "new",
                    seller = "Me",
                    location = "Unknown",
                    isOwner = true,
                )
                if (offerId == -1) {
                    viewModel.createOffer(newOffer)
                } else {
                    viewModel.updateOffer(offerId, newOffer)
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (offerId == -1) "Create" else "Update")
        }
    }
}
