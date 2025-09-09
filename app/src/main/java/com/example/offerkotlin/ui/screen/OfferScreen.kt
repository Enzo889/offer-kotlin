package com.example.offerkotlin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.offerkotlin.data.model.categories
import com.example.offerkotlin.ui.components.FilterDialog
import com.example.offerkotlin.viewmodel.OfferViewModel
import com.example.offerkotlin.data.model.Filters
import com.example.offerkotlin.data.model.Offer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferScreen(viewModel: OfferViewModel = viewModel(),
                navController: androidx.navigation.NavController) {
    val filteredOffers by viewModel.filteredOffers.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentFilters by viewModel.filters.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()

    val currentCategory = categories.find { it.id == selectedCategoryId } ?: categories.first()

    var showCreateDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var offerToEdit by remember { mutableStateOf<Offer?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }

    // Nuevo estado para manejar la confirmación de eliminación
    var offerToDelete by remember { mutableStateOf<Offer?>(null) }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.fetchOffers()
            isRefreshing = false
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Offer")
            }
        },
        topBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        "Marketplace",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.Build, contentDescription = "Filtrar")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = { Text("Buscar productos...") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (currentFilters.priceRange != null || currentFilters.condition != null || currentFilters.location != null) {
                    TextButton(
                        onClick = { viewModel.updateFilters(Filters()) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Limpiar filtros", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            CategoryDropdown(
                selectedCategory = currentCategory,
                onCategoryChange = { category ->
                    viewModel.updateCategoryId(if (category.id == 1) null else category.id)
                }
            )

            if (filteredOffers.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay ofertas disponibles", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = { isRefreshing = true },
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = filteredOffers,
                            key = { it.id!! }
                        ) { offer ->
                            OfferItem(
                                offer = offer,
                                onEdit = { offerToEdit = offer },
                                onDelete = { offerToDelete = offer },
                                onClick = {
                                    navController.navigate("offer_detail/${offer.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog para crear oferta
    if (showCreateDialog) {
        CreateOfferDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { newOffer ->
                viewModel.createOffer(newOffer)
                viewModel.fetchOffers()
                showCreateDialog = false
            }
        )
    }

    // Dialog para editar oferta
    if (offerToEdit != null) {
        EditOfferDialog(
            offer = offerToEdit!!,
            onDismiss = { offerToEdit = null },
            onSave = { updatedOffer ->
                viewModel.updateOffer(updatedOffer)
                viewModel.fetchOffers()
                offerToEdit = null
            }
        )
    }

    // Dialog para confirmar eliminación
    if (offerToDelete != null) {
        AlertDialog(
            onDismissRequest = { offerToDelete = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar esta oferta?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteOffer(offerToDelete!!.id!!)
                        offerToDelete = null
                        viewModel.fetchOffers() // Recarga las ofertas
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { offerToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Modal de filtros
    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onApplyFilters = { filters ->
                viewModel.updateFilters(filters)
            }
        )
    }
}
