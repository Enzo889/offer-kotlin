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
fun OfferScreen(viewModel: OfferViewModel = viewModel()) {
    // ESTADOS: TODO EL ESTADO PROVIENE DEL VIEWMODEL
    val filteredOffers by viewModel.filteredOffers.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentFilters by viewModel.filters.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()

    // Este es un estado derivado del ViewModel, por lo que se actualiza automáticamente.
    val currentCategory = categories.find { it.id == selectedCategoryId } ?: categories.first()

    // ESTADOS LOCALES: Solo para controlar la visibilidad de los diálogos
    var showCreateDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    var offerToEdit by remember { mutableStateOf<Offer?>(null) }

    var isRefreshing by remember { mutableStateOf(false) }



    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.fetchOffers()
            isRefreshing = false // Una vez que la llamada finalice, el estado vuelve a ser false
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
                // Encabezado y botón de filtros
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
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
                // Barra de búsqueda
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
            // Filtro por categoría
            CategoryDropdown(
                selectedCategory = currentCategory,
                onCategoryChange = { category ->
                    viewModel.updateCategoryId(if (category.id == 1) null else category.id)
                }
            )

            // LISTA DE OFERTAS: Se actualiza automáticamente
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
                                onDelete = { viewModel.deleteOffer(offer.id!!) }
                            )
                        }
                    }
                }
            }
        }
    }

    // MODAL PARA CREAR OFERTA
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


    // MODAL DE FILTROS
    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onApplyFilters = { filters ->
                viewModel.updateFilters(filters)
            }
        )
    }
}
