package com.example.bookreader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookreader.screens.ReaderSplashScreen
import com.example.bookreader.screens.details.BookDetailsScreen
import com.example.bookreader.screens.home.Home
import com.example.bookreader.screens.login.ReaderLoginScreen
import com.example.bookreader.screens.search.BookSearchViewModel
import com.example.bookreader.screens.search.ReaderSearchScreen
import com.example.bookreader.screens.stats.ReaderStatsScreen
import com.example.bookreader.screens.update.BookUpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreen.SplashScreen.name) {
        composable(ReaderScreen.SplashScreen.name) {
            ReaderSplashScreen(
                navController = navController
            )
        }

        composable(ReaderScreen.LoginScreen.name) {
            ReaderLoginScreen(
                navController = navController
            )
        }

        composable(ReaderScreen.ReaderHomeScreen.name) {
            Home(
                navController = navController
            )
        }

        val detailName = ReaderScreen.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(
                    navController = navController, bookId = it.toString()
                )
            }

        }

        composable(ReaderScreen.SearchScreen.name) {
            val viewModel = hiltViewModel<BookSearchViewModel>()
            ReaderSearchScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(ReaderScreen.ReaderStatsScreen.name) {
            ReaderStatsScreen(
                navController = navController
            )
        }

        composable(ReaderScreen.UpdateScreen.name) {
            BookUpdateScreen(
                navController = navController
            )
        }
    }
}