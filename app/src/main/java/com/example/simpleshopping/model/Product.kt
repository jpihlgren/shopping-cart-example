package com.example.simpleshopping.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val info: ProductInfo,
    val type: ProductType,
    val imageUrl: String
)
