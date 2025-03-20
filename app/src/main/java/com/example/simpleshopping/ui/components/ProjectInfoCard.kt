package com.example.simpleshopping.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpleshopping.R
import com.example.simpleshopping.model.Product
import com.example.simpleshopping.model.ProductType

@Composable
fun ProductInfoCard(modifier: Modifier, product: Product) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(id = R.string.content_description_info),
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopStart)
            )

            Column(
                modifier = Modifier
                    .padding(start = 32.dp, top = 2.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.details_info_header),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                when (product.type) {
                    ProductType.COUCH -> stringResource(id = R.string.details_type_couch)
                    ProductType.CHAIR -> stringResource(id = R.string.details_type_chair)
                    else -> null
                }?.let {
                    Text(
                        text = stringResource(id = R.string.details_type_label, it),
                        fontSize = 16.sp
                    )
                }
                Text(
                    text = stringResource(id = R.string.details_color, product.info.color),
                    fontSize = 16.sp
                )

                product.info.material?.let { material ->
                    Text(
                        text = stringResource(id = R.string.details_material, material),
                        fontSize = 16.sp
                    )
                }

                product.info.numberOfSeats?.let { seats ->
                    Text(
                        text = stringResource(id = R.string.details_nbr_seats, seats),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
