package com.example.simpleshopping.data

import android.content.Context
import com.example.simpleshopping.model.Product
import com.example.simpleshopping.model.ProductInfo
import com.example.simpleshopping.model.ProductType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val FILENAME = "products.json"

@Singleton
class ProductRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val products: List<Product> by lazy { loadProductsFromJson() }

    fun getAllProducts(): List<Product> {
        return products
    }

    fun getProductById(id: String): Product? {
        return products.find { it.id == id }
    }

    private fun loadProductsFromJson(): List<Product> {
        return try {
            val jsonString = context.assets.open(FILENAME).bufferedReader().use { it.readText() }
            val productListType = object : TypeToken<DataProductList>() {}.type
            val data = Gson().fromJson<DataProductList>(jsonString, productListType).products
            data.map { it.toProduct() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

class DataProduct(
    val id: String,
    val name: String,
    val price: DataPrice,
    val info: DataProductInfo,
    val type: String,
    val imageUrl: String
) {
    fun toProduct(): Product {
        return Product(
            id,
            name,
            price.value,
            info.toProductInfo(),
            ProductType.fromString(type),
            imageUrl
        )
    }
}

data class DataPrice(
    val value: Double,
    val currency: String
)

data class DataProductInfo(
    val material: String? = null,
    val color: String,
    val numberOfSeats: Int? = null
) {
    fun toProductInfo(): ProductInfo {
        return ProductInfo(material, color, numberOfSeats)
    }
}

data class DataProductList(
    val products: List<DataProduct>
)
