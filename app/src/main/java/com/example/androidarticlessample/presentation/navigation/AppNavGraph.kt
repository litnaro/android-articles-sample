package com.example.androidarticlessample.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidarticlessample.presentation.feature.details.ArticleDetailsScreen
import com.example.androidarticlessample.presentation.feature.list.ArticlesListScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Destination.LIST) {
        composable(Destination.LIST) {
            ArticlesListScreen(navigateToDetails = { id ->
                navController.navigate("details/$id")
            })
        }
        composable(Destination.DETAILS) { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull()
            ArticleDetailsScreen(
                articleId = id,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}