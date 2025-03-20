package com.example.simpleshopping.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aminato.sophamtning.ui.screens.ProductListScreen
import com.example.simpleshopping.ui.screens.CartScreen
import com.example.simpleshopping.ui.screens.ProductDetailsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "product_list") {
        composable("product_list") {
            ProductListScreen(
                onProductClick = { product ->
                    navController.navigate("product_details/${product.id}")
                },
                onCartClick = {
                    navController.navigate("cart")
                }
            )
        }

        composable("product_details/{productId}", arguments = listOf(navArgument("productId") { type = NavType.StringType })) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId").orEmpty()
            ProductDetailsScreen(
                productId = productId,
                onBackClick = { navController.popBackStack() },
                onCartClick = { navController.navigate("cart") }
            )
        }

        composable("cart") {
            CartScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
