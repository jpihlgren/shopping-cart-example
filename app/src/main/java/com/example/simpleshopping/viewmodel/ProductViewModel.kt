package com.example.simpleshopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleshopping.Cart
import com.example.simpleshopping.data.ProductRepository
import com.example.simpleshopping.model.CartItem
import com.example.simpleshopping.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productRepository: ProductRepository, private val cart: Cart) : ViewModel() {

    private val _products: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val products: StateFlow<List<Product>> = _products

    fun getAllProducts() {
        viewModelScope.launch {
            _products.value = productRepository.getAllProducts()
        }
    }

    fun getProductById(id: String): Product? {
        return productRepository.getProductById(id)
    }

    fun addToCart(product: Product) {
        cart.addToCart(CartItem(product.id, product.name, product.price, 1))
    }
}