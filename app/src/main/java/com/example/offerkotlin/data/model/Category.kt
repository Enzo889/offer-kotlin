package com.example.offerkotlin.data.model

data class Category(
    val id: Int,
    val name: String
)

val categories = listOf(
    Category(1, "All Products"),
    Category(2, "Offers & Special Price"),
    Category(3, "Fashion & Apparel"),
    Category(4, "Electronics & Gadgets"),
    Category(5, "Home & Living"),
    Category(6, "Health & Beauty"),
    Category(7, "Sports & Fitness"),
    Category(8, "Books & Media"),
    Category(9, "Art & Crafts"),
    Category(10, "Food & Beverages"),
    Category(11, "Toys & Games"),
    Category(12, "Automotive & Tools"),
    Category(13, "Pet Supplies"),
    Category(14, "Travel & Luggage"),
    Category(15, "Musical Instruments"),
    Category(16, "Office & Stationery")
)