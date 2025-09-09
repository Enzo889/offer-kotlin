package com.example.offerkotlin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.offerkotlin.data.model.Offer

// Modelo simulado para reseñas
data class Review(
    val userName: String,
    val rating: Float,
    val comment: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferDetailScreen(
    offer: Offer,
    onBuyNow: () -> Unit = {},
    onAddToCart: () -> Unit = {}
) {
    // Calcular precio final con descuento
    val discountedPrice = if (offer.discount != null && offer.discount > 0) {
        offer.price - (offer.price * offer.discount / 100)
    } else null

    // Mockup de reseñas
    val reviews = listOf(
        Review("Juan Pérez", 4.5f, "Excelente producto, la calidad es muy buena."),
        Review("Ana López", 5f, "Llegó rápido y en perfectas condiciones."),
        Review("Carlos Díaz", 3.5f, "Buen producto, pero el empaque llegó dañado.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(offer.name) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Imagen del producto
                AsyncImage(
                    model = offer.image,
                    contentDescription = offer.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Column {
                    Text(
                        offer.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ubicación: ${offer.location}", style = MaterialTheme.typography.bodyMedium)
                    Text("Condición: ${offer.condition}", style = MaterialTheme.typography.bodyMedium)
                    Text("Stock: 18", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Descripción:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        offer.description ?: "Sin descripción",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Sección de precios
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (discountedPrice != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$${offer.price}",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    textDecoration = TextDecoration.LineThrough,
                                    color = Color.Gray
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$${"%.2f".format(discountedPrice)}",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = Color(0xFF4CAF50) // Verde para destacar el descuento
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${offer.discount}% OFF",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                            )
                        }
                    } else {
                        Text(
                            text = "$${offer.price}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Botones de compra
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onBuyNow,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Comprar ahora")
                    }
                    OutlinedButton(
                        onClick = onAddToCart,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Agregar al carrito")
                    }
                }
            }

            // Reseñas de usuarios (mockup)
            item {
                Text(
                    text = "Reseñas",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(reviews) { review ->
                ReviewItem(review = review)
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = review.userName.first().toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = review.userName,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "⭐ ${review.rating}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFFFC107))
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = review.comment,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
