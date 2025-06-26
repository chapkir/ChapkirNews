package com.example.chapkirnews.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.chapkirnews.presentation.components.bottom_bar.BottomNavBar
import com.example.chapkirnews.presentation.components.top_bar.CustomTopBar
import com.example.chapkirnews.presentation.screens.newsfeed.NewsfeedScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    Scaffold(
        topBar = { CustomTopBar(currentRoute = currentRoute) },
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(route) { saveState = true }
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "newsfeed",
            modifier = Modifier.padding(paddingValues)
        ){
            composable("newsfeed"){
                NewsfeedScreen()
            }

            composable("favorites"){

            }

            composable("detail/{id}"){

            }
        }
    }
}