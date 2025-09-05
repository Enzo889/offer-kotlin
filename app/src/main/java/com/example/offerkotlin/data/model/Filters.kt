package com.example.offerkotlin.data.model

data class Filters(
    val priceRange: ClosedFloatingPointRange<Double>? = null,
    val condition: String? = null,       // "New" o "Used" o "like-new
    val location: String? = null
)
