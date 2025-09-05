package com.example.offerkotlin.viewmodel

import android.R
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
                val newOffer = repository.createOffer(offer)
                _offers.value = (_offers.value + newOffer) as List<Offer>
                _message.value = "Oferta creada correctamente"
            } catch (e: Exception) {
                _message.value = "Error al crear: ${e.message}"
            }
        }
    }

    fun updateOffer(id: Int, offer: Offer) {
        viewModelScope.launch {
            try {
                val updated = repository.updateOffer(id, offer)
                _offers.value = _offers.value.map { (if (it.id == id) updated else it) as Offer }
                _message.value = "Oferta actualizada"
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
    val filteredOffers = combine(_offers, _selectedCategoryId) { offers, selectedCategoryId ->
        offers.filter { offer ->
            selectedCategoryId?.let { id ->
                if (id == 1) true // "All Products"
                else offer.categoryId == id
            } ?: true
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateCategoryId(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
    }
}