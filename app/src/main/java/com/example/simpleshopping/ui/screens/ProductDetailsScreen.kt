package com.example.simpleshopping.ui.screens

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.simpleshopping.R
import com.example.simpleshopping.model.Product
import com.example.simpleshopping.ui.components.ProductInfoCard
import com.example.simpleshopping.ui.theme.Brown
import com.example.simpleshopping.ui.theme.TextStandard
import com.example.simpleshopping.utils.formatPrice
import com.example.simpleshopping.utils.truncateText
import com.example.simpleshopping.viewmodel.ProductViewModel
import kotlinx.coroutines.CoroutineScope
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
    val screenOrientation = LocalConfiguration.current.orientation // Kollar om landscape eller portrait

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
        product?.let {
            if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 16.dp)
                ) {
                    ProductImage(imageUrl = it.imageUrl, square = false)
                    ProductDetailsContent(it, context, snackbarHostState, coroutineScope, onCartClick, viewModel)
                }
            } else {
                Row(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    ProductImage(imageUrl = it.imageUrl, square = true, modifier = Modifier.weight(1f))

                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .verticalScroll(rememberScrollState())
                            .padding(start = 16.dp)
                    ) {
                        ProductDetailsContent(it, context, snackbarHostState, coroutineScope, onCartClick, viewModel)
                    }
                }
            }
        } ?: Text(
            stringResource(id = R.string.details_product_not_found),
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun ProductImage(imageUrl: String, square: Boolean, modifier: Modifier = Modifier.fillMaxWidth()) {
    val errorImage = if (square) R.drawable.ic_missing_square else R.drawable.ic_missing_landscape
    val aspectRatio = if (square) 1f else 16f / 9f
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .error(errorImage)
            .build(),
        contentDescription = null,
        modifier = modifier.aspectRatio(aspectRatio),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun ProductDetailsContent(
    product: Product,
    context: Context,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onCartClick: () -> Unit,
    viewModel: ProductViewModel
) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = product.name,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = TextStandard
    )
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = formatPrice(product.price),
        fontSize = 22.sp,
        color = TextStandard
    )

    ProductInfoCard(modifier = Modifier.padding(16.dp), product = product)

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = {
            viewModel.addToCart(product)

            val message = context.getString(R.string.details_added_to_cart, truncateText(product.name, 30))
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
}
