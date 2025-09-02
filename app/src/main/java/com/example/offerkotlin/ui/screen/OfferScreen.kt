package com.example.offerkotlin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.offerkotlin.data.model.Offer
import com.example.offerkotlin.viewmodel.OfferViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferScreen(
    viewModel: OfferViewModel,
    navController: NavController
) {
    val offers = viewModel.offers.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Marketplace") },
                actions = {
                    IconButton(onClick = { navController.navigate("offerForm") }) {
                        Icon(Icons.Default.Add, contentDescription = "New Offer")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(offers.value) { offer ->
                OfferItem(
                    offer = offer,
                    onEdit = { navController.navigate("offerForm/${offer.id}") },
                    onDelete = { offer.id?.let { viewModel.deleteOffer(it) } }
                )
            }
        }
    }
}

@Composable
fun OfferItem(
    offer: Offer,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Imagen
            AsyncImage(
                model = offer.image,
                contentDescription = offer.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp)
            )

            // Información
            Column(modifier = Modifier.weight(1f)) {
                Text(offer.name, style = MaterialTheme.typography.titleMedium)
                Text("${offer.condition} • ${offer.location}", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(4.dp))

                // Precio con descuento
                Row {
                    offer.discount?.let {
                        if (it > 0) {
                            Text(
                                text = "$${offer.price}",
                                style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val discounted = offer.discount?.let { offer.price * (1 - it / 100.0) }
                            Text(
                                text = "$${"%.2f".format(discounted)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${offer.discount}% OFF",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Green
                            )
                        } else {
                            Text(
                                text = "$${offer.price}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text("Envío: ${offer.shipping}", color = Color.Green, style = MaterialTheme.typography.bodySmall)
            }

            // Menú de acciones
            var expanded by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(text = { Text("Add to Favorites") }, onClick = {
                        expanded = false
                    })
                    DropdownMenuItem(text = { Text("Add to Cart") }, onClick = {
                        expanded = false
                    })
                    DropdownMenuItem(text = { Text("Edit") }, onClick = {
                        expanded = false
                        onEdit()
                    })
                    DropdownMenuItem(
                        text = { Text("Delete", color = Color.Red) },
                        onClick = {
                            expanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}
