package com.example.offerkotlin.data.model

data class Offer(
    val id: Int? = null,
    val name: String,
    val image: String? = null,
    val stock: String? = null,
    val price: Double,
    val discount: Int? = null,
    val installments: String? = null,
    val shipping: String? = null,
    val description: String? = null,
    val categoryId: Int? = null,
    val condition: String,
    val seller: String? = null,
    val location: String,
    val isOwner: Boolean? = null
)
