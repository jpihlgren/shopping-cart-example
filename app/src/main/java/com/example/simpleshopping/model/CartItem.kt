package com.example.simpleshopping.model

data class CartItem(
    val productId: String,
    val name: String,
    val price: Double,
    val count: Int
)

