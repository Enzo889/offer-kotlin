package com.example.offerkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offerkotlin.data.model.Filters
import com.example.offerkotlin.data.model.Offer
import com.example.offerkotlin.data.repository.OfferRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch



class OfferViewModel : ViewModel() {
    private val repository = OfferRepository()

    private val _offers = MutableStateFlow<List<Offer>>(emptyList())
    val offers: StateFlow<List<Offer>> = _offers

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId


    private val _filters = MutableStateFlow(Filters())
    val filters: StateFlow<Filters> = _filters

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _offerCreated = MutableSharedFlow<Unit>()
    val offerCreated: SharedFlow<Unit> = _offerCreated



    init {
        fetchOffers()
    }



    fun fetchOffers() {
        viewModelScope.launch {
            try {
                val response = repository.getOffers()
                _offers.value = response.data ?: emptyList()
            } catch (e: Exception) {
                _message.value = "Error al cargar ofertas: ${e.message}"
            }
        }
    }

    fun createOffer(offer: Offer) {
        viewModelScope.launch {
            try {
                val response = repository.createOffer(offer)
                val createdOffer = response.data
                if (createdOffer != null) {
                    _offers.value = _offers.value.toMutableList().apply { add(createdOffer) }
                    _message.value = "Oferta creada correctamente"
                    // Emite un evento para notificar a la UI
                    _offerCreated.emit(Unit)
                } else {
                    _message.value = "Error: la API no devolvió la oferta creada"
                }
            } catch (e: Exception) {
                _message.value = "Error al crear: ${e.message}"
            }
        }
    }


    fun updateOffer(updatedOffer: Offer) {
        viewModelScope.launch {
            try {
                val response = repository.updateOffer(updatedOffer.id!!, updatedOffer)
                val result = response.data
                if (result != null) {
                    // Emitir una lista NUEVA para que Compose detecte cambios
                    _offers.value = _offers.value.map { offer ->
                        if (offer.id == result.id) result else offer
                    }.toList()

                    _message.value = "Oferta actualizada correctamente"
                } else {
                    _message.value = "Error: no se pudo actualizar la oferta"
                }
            } catch (e: Exception) {
                _message.value = "Error al actualizar: ${e.message}"
            }
        }
    }



    fun deleteOffer(id: Int?) {
        viewModelScope.launch {
            try {
                repository.deleteOffer(id)
                _offers.value = _offers.value.filterNot { it.id == id }
                _message.value = "Oferta eliminada"
            } catch (e: Exception) {
                _message.value = "Error al eliminar: ${e.message}"
            }
        }
    }

    // -------- BUSCAR --------
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // -------- FILTRAR POR CATEGORÍA --------
    fun updateCategory(category: Int?) {
        _selectedCategoryId.value = category
    }

    fun updateFilters(filters: Filters) {
        _filters.value = filters
    }

    // -------- OFERTAS FILTRADAS --------
    val filteredOffers = combine(
        _offers,
        _searchQuery,
        _selectedCategoryId,
        _filters
    ) { offers, searchQuery, categoryId, filters ->
        Log.d("DEBUG", "Offers actualizadas -> ${offers.size}") // 👈 Visible en Logcat

        offers.filter { offer ->
            // --- Filtrado por categoría ---
            val matchCategory = when (categoryId) {
                null -> true // Si no hay categoría seleccionada, mostrar todas
                1 -> true    // Si es categoría 1, mostrar todas
                2 -> offer.discount != null && offer.discount > 0 // 👈 SOLO con descuento
                else -> offer.categoryId == categoryId
            }

            // --- Búsqueda por nombre o descripción ---
            val matchSearch = if (searchQuery.isNotBlank()) {
                offer.name.contains(searchQuery, ignoreCase = true) ||
                        (offer.description?.contains(searchQuery, ignoreCase = true) == true)
            } else true

            // --- Otros filtros opcionales ---
            val matchCondition = filters.condition?.let { offer.condition == it } ?: true
            val matchPrice = filters.priceRange?.let { offer.price in it } ?: true
            val matchLocation = filters.location?.let {
                offer.location.contains(it, ignoreCase = true)
            } ?: true

            // --- Resultado final ---
            matchCategory && matchSearch && matchCondition && matchPrice && matchLocation
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateCategoryId(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
    }
}