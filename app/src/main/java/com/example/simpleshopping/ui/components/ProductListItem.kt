package com.example.simpleshopping.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.simpleshopping.R
import com.example.simpleshopping.model.Product
import com.example.simpleshopping.ui.theme.TextSecondary
import com.example.simpleshopping.ui.theme.TextStandard
import com.example.simpleshopping.utils.formatPrice

@Composable
fun ProductListItem(product: Product, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.imageUrl)
                .crossfade(true)
                .error(R.drawable.ic_missing_square)
                .build(),
            contentDescription = product.name,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = product.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextStandard)
            Text(text = product.info.color.capitalize(Locale.current), fontSize = 14.sp, color = TextSecondary)
            Text(text = formatPrice(product.price), fontSize = 14.sp, color = TextStandard)
        }
    }
}
