package com.example.simpleshopping.model

enum class ProductType {
    CHAIR, COUCH, UNKNOWN;

    companion object {
        fun fromString(value: String): ProductType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
        }
    }
}