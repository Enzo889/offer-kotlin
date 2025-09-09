package com.example.offerkotlin.data.model

data class Category(
    val id: Int,
    val name: String
)

val categories = listOf(
    Category(1, "Todos los productos"),
    Category(2, "Ofertas y precios especiales"),
    Category(3, "Moda y ropa"),
    Category(4, "Electrónica y gadgets"),
    Category(5, "Hogar y decoración"),
    Category(6, "Salud y belleza"),
    Category(7, "Deportes y fitness"),
    Category(8, "Libros y medios"),
    Category(9, "Arte y manualidades"),
    Category(10, "Alimentos y bebidas"),
    Category(11, "Juguetes y juegos"),
    Category(12, "Automóviles y herramientas"),
    Category(13, "Mascotas y accesorios"),
    Category(14, "Viajes y equipaje"),
    Category(15, "Instrumentos musicales"),
    Category(16, "Oficina y papelería")
)