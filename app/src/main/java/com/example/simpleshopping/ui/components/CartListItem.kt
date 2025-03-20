package com.example.simpleshopping.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpleshopping.R
import com.example.simpleshopping.model.CartItem
import com.example.simpleshopping.ui.theme.TextSecondary
import com.example.simpleshopping.ui.theme.TextStandard
import com.example.simpleshopping.utils.formatPrice

@Composable
fun CartListItem(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    var showMaxQuantityDialog by remember { mutableStateOf(false) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = cartItem.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextStandard)
            Text(text = formatPrice(cartItem.price), fontSize = 14.sp, color = TextSecondary)
            Text(text = stringResource(id = R.string.cart_item_count, cartItem.count), fontSize = 14.sp, color = TextSecondary)
        }

        Row {
            IconButton(onClick = {
                if (cartItem.count <= 1) {
                    showConfirmDeleteDialog = true
                } else {
                    onDecrease()
                }
            }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = stringResource(R.string.content_description_decrease_count))
            }

            Text(
                text = cartItem.count.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically),
                color = TextStandard
            )

            IconButton(onClick = {
                if (cartItem.count >= 10) {
                    showMaxQuantityDialog = true
                } else {
                    onIncrease()
                }
            }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = stringResource(R.string.content_description_increase_count))
            }

            IconButton(onClick = { showConfirmDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.content_description_remove_item))
            }
        }
    }

    if (showMaxQuantityDialog) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.surface,
            onDismissRequest = { showMaxQuantityDialog = false },
            confirmButton = {
                Button(onClick = { showMaxQuantityDialog = false }) {
                    Text(stringResource(id = R.string.cart_max_count_ok))
                }
            },
            text = { Text(text = stringResource(R.string.cart_max_count), color = TextStandard) }
        )
    }

    if (showConfirmDeleteDialog) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.surface,
            onDismissRequest = { showConfirmDeleteDialog = false },
            confirmButton = {
                Button(onClick = {
                    onRemove()
                    showConfirmDeleteDialog = false
                }) {
                    Text(stringResource(id = R.string.cart_confirm_remove_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDeleteDialog = false }) {
                    Text(stringResource(id = R.string.cart_confirm_remove_cancel))
                }
            },
            text = { Text(text = stringResource(R.string.cart_confirm_remove, cartItem.name), color = TextStandard) }
        )
    }
}
