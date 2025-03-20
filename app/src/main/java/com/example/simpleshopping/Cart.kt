package com.example.simpleshopping

import com.example.simpleshopping.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cart @Inject constructor() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addToCart(cartItem: CartItem) {
        _cartItems.value = _cartItems.value.toMutableList().apply {
            val index = indexOfFirst { it.productId == cartItem.productId }
            if (index != -1) {
                this[index] = this[index].copy(count = this[index].count + 1)
            } else {
                add(cartItem)
            }
        }
    }

    fun increaseCount(productId: String) {
        _cartItems.value = _cartItems.value.map {
            if (it.productId == productId) it.copy(count = it.count + 1) else it
        }
    }

    fun decreaseCount(productId: String) {
        _cartItems.value = _cartItems.value.toMutableList().apply {
            val index = indexOfFirst { it.productId == productId }
            if (index != -1) {
                if (this[index].count > 1) {
                    this[index] = this[index].copy(count = this[index].count - 1)
                } else {
                    removeAt(index)
                }
            }
        }
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filterNot { it.productId == productId }
    }
}
