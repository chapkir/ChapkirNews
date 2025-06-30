package com.example.chapkirnews.presentation.navigation

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.chapkirnews.presentation.components.bottom_bar.BottomNavBar
import com.example.chapkirnews.presentation.screens.favorites_screen.FavoritesScreen
import com.example.chapkirnews.presentation.screens.news_detail_screen.NewsDetailScreen
import com.example.chapkirnews.presentation.screens.news_detail_screen.NewsDetailSharedViewModel
import com.example.chapkirnews.presentation.screens.newsfeed_screen.NewsfeedScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentGraphRoute = navBackStackEntry?.destination?.hierarchy
        ?.firstOrNull { it.route in listOf("news_graph", "favorites_graph") }
        ?.route ?: ""

    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentGraphRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onPopToStartDestination = { startRoute ->
                    navController.popBackStack(route = startRoute, inclusive = false)
                }
            )
        },
        containerColor =
        when {
            currentRoute.startsWith("news_graph/newsDetail") -> {
                MaterialTheme.colorScheme.surface
            }
            currentRoute.startsWith("favorites_graph/newsDetail") -> {
                MaterialTheme.colorScheme.surface
            }
            else -> {
                MaterialTheme.colorScheme.background
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "news_graph",
            modifier = Modifier.padding(paddingValues)
        ) {

            navigation(startDestination = "newsfeed", route = "news_graph") {

                composable("newsfeed") { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry("news_graph")
                    }
                    val sharedViewModel: NewsDetailSharedViewModel = hiltViewModel(parentEntry)

                    NewsfeedScreen(
                        onArticleClick = { article ->
                            sharedViewModel.selectArticle(article)
                            val encodedUrl = Uri.encode(article.url)
                            navController.navigate("news_graph/newsDetail/$encodedUrl")
                        }
                    )
                }

                composable(
                    route = "news_graph/newsDetail/{articleUrl}",
                    arguments = listOf(navArgument("articleUrl") { type = NavType.StringType })
                ) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry("news_graph")
                    }
                    val sharedViewModel: NewsDetailSharedViewModel = hiltViewModel(parentEntry)
                    val article by sharedViewModel.selectedArticle.collectAsState()

                    if (article != null) {
                        NewsDetailScreen(
                            article = article!!,
                            onToggleFavorite = { sharedViewModel.toggleFavorite(article!!) },
                            onClose = {
                                sharedViewModel.clear()
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }

            navigation(startDestination = "favorites", route = "favorites_graph") {

                composable("favorites") { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry("favorites_graph")
                    }
                    val sharedViewModel: NewsDetailSharedViewModel = hiltViewModel(parentEntry)

                    FavoritesScreen(
                        onArticleClick = { article ->
                            sharedViewModel.selectArticle(article)
                            val encodedUrl = Uri.encode(article.url)
                            navController.navigate("favorites_graph/newsDetail/$encodedUrl")
                        }
                    )
                }

                composable(
                    route = "favorites_graph/newsDetail/{articleUrl}",
                    arguments = listOf(navArgument("articleUrl") { type = NavType.StringType })
                ) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry("favorites_graph")
                    }
                    val sharedViewModel: NewsDetailSharedViewModel = hiltViewModel(parentEntry)
                    val article by sharedViewModel.selectedArticle.collectAsState()

                    if (article != null) {
                        NewsDetailScreen(
                            article = article!!,
                            onToggleFavorite = { sharedViewModel.toggleFavorite(article!!) },
                            onClose = {
                                sharedViewModel.clear()
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}