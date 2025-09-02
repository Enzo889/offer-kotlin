package com.example.offerkotlin.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {
    val api: OfferApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/") // ⚠️ emulator → localhost
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OfferApi::class.java)
    }
}