package com.example.simpleshopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleshopping.Cart
import com.example.simpleshopping.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cart: Cart
) : ViewModel() {
    val cartItems: StateFlow<List<CartItem>> = cart.cartItems

    val totalPrice: StateFlow<Double> = cart.cartItems.map { items ->
        items.sumOf { it.price * it.count }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun increaseCount(productId: String) = cart.increaseCount(productId)

    fun decreaseCount(productId: String) = cart.decreaseCount(productId)

    fun removeFromCart(productId: String) = cart.removeFromCart(productId)
}
