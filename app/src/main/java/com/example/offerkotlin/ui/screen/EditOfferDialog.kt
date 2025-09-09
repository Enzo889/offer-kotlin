package com.example.offerkotlin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.offerkotlin.data.model.Offer
import com.example.offerkotlin.data.model.categories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOfferDialog(
    offer: Offer,
    onDismiss: () -> Unit,
    onSave: (Offer) -> Unit
) {
    // ----------- Estados iniciales con los datos de la oferta ----------- //
    var name by remember { mutableStateOf(offer.name) }
    var image by remember { mutableStateOf(offer.image ?: "") }
    var price by remember { mutableStateOf(offer.price.toString()) }
    var discount by remember { mutableStateOf(offer.discount?.toString() ?: "") }
    var description by remember { mutableStateOf(offer.description ?: "") }
    var location by remember { mutableStateOf(offer.location) }

    // Categoría
    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by remember {
        mutableStateOf(categories.find { it.id == offer.categoryId } ?: categories.first())
    }

    // Condición
    val conditions = listOf("Nuevo", "Como Nuevo", "Usado")
    var expandedCondition by remember { mutableStateOf(false) }
    var selectedCondition by remember {
        mutableStateOf(offer.condition.ifBlank { conditions.first() })
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Editar Oferta", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // ----------- Nombre ----------- //
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // ----------- Imagen ----------- //
                OutlinedTextField(
                    value = image,
                    onValueChange = { image = it },
                    label = { Text("URL de la imagen") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // ----------- Precio ----------- //
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // ----------- Descuento ----------- //
                OutlinedTextField(
                    value = discount,
                    onValueChange = { discount = it },
                    label = { Text("Descuento (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // ----------- Descripción ----------- //
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                // ----------- Categoría (Dropdown) ----------- //
                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }

                // ----------- Condición (Dropdown) ----------- //
                ExposedDropdownMenuBox(
                    expanded = expandedCondition,
                    onExpandedChange = { expandedCondition = it }
                ) {
                    OutlinedTextField(
                        value = selectedCondition,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Condición") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCondition,
                        onDismissRequest = { expandedCondition = false }
                    ) {
                        conditions.forEach { condition ->
                            DropdownMenuItem(
                                text = { Text(condition) },
                                onClick = {
                                    selectedCondition = condition
                                    expandedCondition = false
                                }
                            )
                        }
                    }
                }

                // ----------- Ubicación ----------- //
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Ubicación") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    offer.copy(
                        name = name,
                        image = image.ifBlank { "https://via.placeholder.com/150" },
                        price = price.toDoubleOrNull() ?: offer.price,
                        discount = discount.toIntOrNull(),
                        description = description,
                        category = selectedCategory.name,
                        categoryId = selectedCategory.id,
                        condition = selectedCondition,
                        location = location
                    )
                )
            }) {
                Text("Guardar cambios")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
