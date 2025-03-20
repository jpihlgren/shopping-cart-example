package com.example.simpleshopping.utils

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("sv", "SE")).apply {
        minimumFractionDigits = if (price % 1.0 == 0.0) 0 else 2
        maximumFractionDigits = 2
    }
    return "${formatter.format(price)} kr"
}

fun truncateText(text: String, maxLength: Int): String {
    return if (text.length > maxLength) {
        text.take(maxLength) + "…"
    } else {
        text
    }
}