package com.example.offerkotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offerkotlin.data.model.Offer
import com.example.offerkotlin.data.repository.OfferRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OfferViewModel : ViewModel() {
    private val repository = OfferRepository()

    private val _offers = MutableStateFlow<List<Offer>>(emptyList())
    val offers: StateFlow<List<Offer>> = _offers

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun fetchOffers() {
        viewModelScope.launch {
            try {
                val response = repository.getOffers()
                _offers.value = response.data ?: emptyList()
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun createOffer(offer: Offer) {
        viewModelScope.launch {
            try {
                val newOffer = repository.createOffer(offer)
                _offers.value = _offers.value + newOffer
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
                _offers.value = _offers.value.map { if (it.id == id) updated else it }
                _message.value = "Oferta actualizada"
            } catch (e: Exception) {
                _message.value = "Error al actualizar: ${e.message}"
            }
        }
    }

    fun deleteOffer(id: Int) {
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
}