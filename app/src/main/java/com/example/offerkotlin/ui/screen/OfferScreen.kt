package com.example.offerkotlin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.offerkotlin.data.model.Category
import com.example.offerkotlin.data.model.Filters
import com.example.offerkotlin.data.model.categories
import com.example.offerkotlin.ui.components.FilterDialog
import com.example.offerkotlin.viewmodel.OfferViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferScreen(viewModel: OfferViewModel) {
    // Estados que provienen del ViewModel
    val offers by viewModel.offers.collectAsState() // Todas las ofertas
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentFilters by viewModel.filters.collectAsState()

    // Estado para la categoría actual
    var currentCategory by remember { mutableStateOf(categories.first()) }

    // Estados locales
    var showCreateDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    /** --------- LÓGICA DE FILTRADO --------- */
    val filteredOffers = offers.filter { offer ->
        val matchCategory = currentCategory.id == 1 || offer.categoryId == currentCategory.id

        val matchCondition = currentFilters.condition?.let {
            offer.condition == it
        } ?: true

        val matchPrice = currentFilters.priceRange?.let {
            offer.price in it
        } ?: true

        val matchLocation = currentFilters.location?.let {
            offer.location.contains(it, ignoreCase = true)
        } ?: true

        val matchSearch = if (searchQuery.isNotBlank()) {
            offer.name.contains(searchQuery, ignoreCase = true) ||
                    (offer.description?.contains(searchQuery, ignoreCase = true) == true)
        } else true

        matchCategory && matchCondition && matchPrice && matchLocation && matchSearch
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Offer")
            }
        },
        topBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                // Encabezado con título y botón de filtros
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Marketplace",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    // Botón para abrir modal de filtros
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
            /** -------- FILTRO POR CATEGORÍA -------- */
            CategoryDropdown(
                selectedCategory = currentCategory,
                onCategoryChange = { category ->
                    currentCategory = category
                    // Notificamos al ViewModel, por si necesitas guardarlo allí
                    viewModel.updateCategoryId(if (category.id == 1) null else category.id)
                }
            )

            /** -------- LISTA DE OFERTAS -------- */
            if (filteredOffers.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay ofertas disponibles", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredOffers) { offer ->
                        OfferItem(
                            offer = offer,
                            onDelete = { viewModel.deleteOffer(offer.id!!) },
                            onEdit = {}
                        )
                    }
                }
            }
        }
    }

    /** -------- MODAL PARA CREAR OFERTA -------- */
    if (showCreateDialog) {
        CreateOfferDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { newOffer ->
                viewModel.createOffer(newOffer)
                showCreateDialog = false
            }
        )
    }

    /** -------- MODAL DE FILTROS -------- */
    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onApplyFilters = { filters ->
                viewModel.updateFilters(filters)
            }
        )
    }
}
