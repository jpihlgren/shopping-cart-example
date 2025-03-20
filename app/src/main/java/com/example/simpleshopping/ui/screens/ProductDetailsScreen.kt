package com.example.simpleshopping.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.simpleshopping.R
import com.example.simpleshopping.ui.components.ProductInfoCard
import com.example.simpleshopping.ui.theme.Brown
import com.example.simpleshopping.ui.theme.TextStandard
import com.example.simpleshopping.utils.formatPrice
import com.example.simpleshopping.utils.truncateText
import com.example.simpleshopping.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: String,
    viewModel: ProductViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onCartClick: () -> Unit
) {
    val product = viewModel.getProductById(productId)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: stringResource(id = R.string.details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.content_description_back))
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = stringResource(id = R.string.content_description_cart))
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = Brown,
                    contentColor = Color.White,
                    actionColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(bottom = 16.dp)) {
            product?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.imageUrl)
                        .crossfade(true)
                        .error(R.drawable.ic_missing_landscape)
                        .build(),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = it.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextStandard
                )
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = formatPrice(it.price),
                    fontSize = 22.sp,
                    color = TextStandard
                )

                ProductInfoCard(
                    modifier = Modifier,
                    product = it
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        viewModel.addToCart(it)

                        val message = context.getString(R.string.details_added_to_cart, truncateText(it.name, 30))
                        val actionLabel = context.getString(R.string.details_action_view_cart)

                        coroutineScope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = message,
                                actionLabel = actionLabel,
                                duration = SnackbarDuration.Short,
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                onCartClick()
                            }
                        }
                    }
                ) {
                    Text(stringResource(id = R.string.details_add_to_cart))
                }
            } ?: Text(stringResource(id = R.string.details_product_not_found), color = MaterialTheme.colorScheme.error)
        }
    }
}
