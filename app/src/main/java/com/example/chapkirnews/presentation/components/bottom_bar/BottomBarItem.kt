package com.example.chapkirnews.presentation.components.bottom_bar

import com.example.chapkirnews.R

sealed class BottomBarItem(
    val route: String,
    val icon: Int,
    val filledIcon: Int,
    val label: String,
) {
    object Newsfeed : BottomBarItem(
        route = "news_graph",
        icon = R.drawable.ic_news,
        filledIcon = R.drawable.ic_news_filled,
        label = "News"
    )

    object Favorites : BottomBarItem(
        route = "favorites_graph",
        icon = R.drawable.ic_bookmark,
        filledIcon = R.drawable.ic_bookmark_filled,
        label = "Favs"
    )
}