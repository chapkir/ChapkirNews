package com.example.chapkirnews.presentation.navigation

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.chapkirnews.presentation.components.bottom_bar.BottomNavBar
import com.example.chapkirnews.presentation.screens.favorites_screen.FavoritesScreen
import com.example.chapkirnews.presentation.screens.news_detail_screen.NewsDetailDialog
import com.example.chapkirnews.presentation.screens.news_detail_screen.NewsDetailSharedViewModel
import com.example.chapkirnews.presentation.screens.newsfeed_screen.NewsfeedScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    Scaffold(
        //topBar = { CustomTopBar(currentRoute = currentRoute) },
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        containerColor =
        when (currentRoute){
            "newsfeed" -> MaterialTheme.colorScheme.background
            "favorites" -> MaterialTheme.colorScheme.background
            else -> MaterialTheme.colorScheme.surface
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "newsfeed",
            modifier = Modifier.padding(paddingValues)
        ) {

            composable("newsfeed") { backStackEntry ->

                val sharedViewModel: NewsDetailSharedViewModel = hiltViewModel(backStackEntry)

                NewsfeedScreen(
                    onArticleClick = { article ->
                        sharedViewModel.selectArticle(article)
                        val encodedUrl = Uri.encode(article.url)
                        navController.navigate("newsDetail/$encodedUrl")
                    }
                )
            }

            composable(
                route = "newsDetail/{articleUrl}",
                arguments = listOf(navArgument("articleUrl") { type = NavType.StringType })
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("newsfeed")
                }
                val sharedViewModel: NewsDetailSharedViewModel = hiltViewModel(parentEntry)

                val article by sharedViewModel.selectedArticle.collectAsState()

                if (article != null) {
                    NewsDetailDialog(
                        article = article!!,
                        onClose = {
                            sharedViewModel.clear()
                            navController.popBackStack()
                        }
                    )
                } else {
                    // новость не найдена
                }
            }

            composable("favorites") {
                FavoritesScreen()
            }

        }
    }
}