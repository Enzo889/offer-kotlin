package com.example.offerkotlin.data.repository

import com.example.offerkotlin.data.model.ApiResponse
import com.example.offerkotlin.data.model.Offer
import com.example.offerkotlin.data.remote.RetroFitInstance

class OfferRepository {
    private val api = RetroFitInstance.api

    suspend fun getOffers(): ApiResponse<List<Offer>> = api.getOffers()
    suspend fun getOffer(id: Int): ApiResponse<Offer> = api.getOffer(id)
    suspend fun createOffer(offer: Offer): Offer = api.createOffer(offer)
    suspend fun updateOffer(id: Int, offer: Offer): Offer = api.updateOffer(id, offer)
    suspend fun deleteOffer(id: Int): ApiResponse<Any> = api.deleteOffer(id)
}