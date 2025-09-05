package com.example.offerkotlin.data.remote

import com.example.offerkotlin.data.model.ApiResponse
import com.example.offerkotlin.data.model.Offer
import retrofit2.http.*

interface OfferApi {
    @GET("offers")
    suspend fun getOffers(): ApiResponse<List<Offer>>

    @GET("offers/{id}")
    suspend fun getOffer(@Path("id") id: Int): ApiResponse<Offer>

    @POST("offers")
    suspend fun createOffer(@Body offer: Offer): ApiResponse<Offer>

    @PUT("offers/{id}")
    suspend fun updateOffer(@Path("id") id: Int, @Body offer: Offer): ApiResponse<Offer>

    @DELETE("offers/{id}")
    suspend fun deleteOffer(@Path("id") id: Int?): ApiResponse<Unit>
}
