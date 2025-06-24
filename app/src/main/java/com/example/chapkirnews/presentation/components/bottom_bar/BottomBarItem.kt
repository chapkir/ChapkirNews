package com.example.chapkirnews.presentation.components.bottom_bar

import com.example.chapkirnews.R

sealed class BottomBarItem(
    val route: String,
    val icon: Int,
    val label: String,
) {
    object Newsfeed : BottomBarItem(
        route = "newsfeed",
        icon = R.drawable.ic_news,
        label = "Новости"
    )

    object Favorites : BottomBarItem(
        route = "favorites",
        icon = R.drawable.ic_bookmark,
        label = "Избранное"
    )
}