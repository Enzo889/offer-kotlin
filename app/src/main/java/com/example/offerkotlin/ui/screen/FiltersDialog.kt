package com.example.offerkotlin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.offerkotlin.data.model.Filters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onApplyFilters: (Filters) -> Unit
) {
    var minPrice by remember { mutableStateOf(0f) }
    var maxPrice by remember { mutableStateOf(1000f) }

    var condition by remember { mutableStateOf<String?>(null) }
    var location by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtrar Ofertas", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                // --------- SLIDER DE PRECIO ---------
                Text(
                    text = "Rango de precios",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RangeSlider(
                        value = minPrice..maxPrice,
                        onValueChange = { range ->
                            minPrice = range.start
                            maxPrice = range.endInclusive
                        },
                        valueRange = 0f..10000f,
                        steps = 5,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Mostramos los valores actuales
                    Text(
                        text = "Desde $${minPrice.toInt()} hasta $${maxPrice.toInt()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --------- CONDICIÓN ---------
                Text("Estado del producto", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        label = { Text("Nuevo") },
                        selected = condition == "new",
                        onClick = { condition = if (condition == "new") null else "new" }
                    )
                    FilterChip(
                        label = { Text("Como nuevo") },
                        selected = condition == "like-new",
                        onClick = { condition = if (condition == "like-new") null else "like-new" }
                    )
                    FilterChip(
                        label = { Text("Usado") },
                        selected = condition == "used",
                        onClick = { condition = if (condition == "used") null else "used" }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --------- UBICACIÓN ---------
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Ubicación") },
                    placeholder = { Text("Ej: Mendoza, Argentina") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        // --------- BOTONES DE ACCIÓN ---------
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botón para limpiar filtro

                // Fila para los botones de Cancelar y Aplicar
                Row {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val filters = Filters(
                                priceRange = minPrice.toDouble()..maxPrice.toDouble(),
                                condition = condition,
                                location = if (location.isNotBlank()) location else null
                            )
                            onApplyFilters(filters)
                            onDismiss()
                        }
                    ) {
                        Text("Aplicar")
                    }
                }
            }
        }
    )
}
