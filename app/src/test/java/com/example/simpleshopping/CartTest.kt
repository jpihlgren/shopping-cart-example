package com.example.simpleshopping

import com.example.simpleshopping.model.CartItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CartTest {

    private lateinit var cart: Cart

    @Before
    fun setUp() {
        cart = Cart()
    }

    @Test
    fun `addToCart should add a new item if it does not exist`() = runTest {
        val item = CartItem(productId = "1", name = "Test Product", price = 100.0, count = 1)

        cart.addToCart(item)

        val cartItems = cart.cartItems.first()
        assertEquals(1, cartItems.size)
        assertEquals(item.productId, cartItems[0].productId)
        assertEquals(1, cartItems[0].count)
    }

    @Test
    fun `addToCart should increase count if item already exists`() = runTest {
        val item = CartItem(productId = "1", name = "Test Product", price = 100.0, count = 1)

        cart.addToCart(item)
        cart.addToCart(item)

        val cartItems = cart.cartItems.first()
        assertEquals(1, cartItems.size)
        assertEquals(2, cartItems[0].count)
    }

    @Test
    fun `increaseCount should increase item count`() = runTest {
        val item = CartItem(productId = "1", name = "Test Product", price = 100.0, count = 1)
        cart.addToCart(item)

        cart.increaseCount("1")

        val cartItems = cart.cartItems.first()
        assertEquals(1, cartItems.size)
        assertEquals(2, cartItems[0].count)
    }

    @Test
    fun `increaseCount should do nothing if item does not exist`() = runTest {
        cart.increaseCount("99")

        val cartItems = cart.cartItems.first()
        assertEquals(0, cartItems.size)
    }

    @Test
    fun `decreaseCount should decrease count if greater than 1`() = runTest {
        val item = CartItem(productId = "1", name = "Test Product", price = 100.0, count = 2)
        cart.addToCart(item)

        cart.decreaseCount("1")

        val cartItems = cart.cartItems.first()
        assertEquals(1, cartItems.size)
        assertEquals(1, cartItems[0].count)
    }

    @Test
    fun `decreaseCount should remove item if count is 1`() = runTest {
        val item = CartItem(productId = "1", name = "Test Product", price = 100.0, count = 1)
        cart.addToCart(item)

        cart.decreaseCount("1")

        val cartItems = cart.cartItems.first()
        assertTrue(cartItems.isEmpty())
    }

    @Test
    fun `decreaseCount should do nothing if item does not exist`() = runTest {
        cart.decreaseCount("99")

        val cartItems = cart.cartItems.first()
        assertTrue(cartItems.isEmpty())
    }

    @Test
    fun `removeFromCart should remove the item`() = runTest {
        val item = CartItem(productId = "1", name = "Test Product", price = 100.0, count = 2)
        cart.addToCart(item)

        cart.removeFromCart("1")

        val cartItems = cart.cartItems.first()
        assertTrue(cartItems.isEmpty())
    }

    @Test
    fun `removeFromCart should do nothing if item does not exist`() = runTest {
        cart.removeFromCart("99")

        val cartItems = cart.cartItems.first()
        assertTrue(cartItems.isEmpty())
    }

    @Test
    fun `after addToCart the items should be in the order they were added`() = runTest {
        val item1 = CartItem(productId = "1", name = "Test Product", price = 100.0, count = 1)
        val item2 = CartItem(productId = "2", name = "Test Product", price = 100.0, count = 1)
        val item3 = CartItem(productId = "3", name = "Test Product", price = 100.0, count = 1)
        val item4 = CartItem(productId = "4", name = "Test Product", price = 100.0, count = 1)

        cart.addToCart(item1)
        cart.addToCart(item4)
        cart.addToCart(item2)
        cart.addToCart(item3)

        val cartItems = cart.cartItems.first()
        assertEquals(4, cartItems.size)
        assertEquals(item1.productId, cartItems[0].productId)
        assertEquals(item4.productId, cartItems[1].productId)
        assertEquals(item2.productId, cartItems[2].productId)
        assertEquals(item3.productId, cartItems[3].productId)
    }
}
