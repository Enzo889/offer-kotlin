package com.example.offerkotlin.data.model

data class Offer(
    val id: Int? = null,
    val name: String,
    val image: String,
    val stock: String,
    val price: Double,
    val discount: Int?,
    val installments: String?,
    val shipping: String?,
    val description: String,
    val category: String,
    val categoryId: Int,
    val condition: String,
    val seller: String,
    val location: String,
    val isOwner: Boolean
)
