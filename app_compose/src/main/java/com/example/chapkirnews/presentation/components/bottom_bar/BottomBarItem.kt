package com.example.chapkirnews.presentation.components.bottom_bar

import com.example.chapkirnews.R

sealed class BottomBarItem(
    val graphRoute: String,
    val startRoute: String,
    val icon: Int,
    val filledIcon: Int,
    val label: String,
) {
    object Newsfeed : BottomBarItem(
        graphRoute = "news_graph",
        startRoute = "newsfeed",
        icon = R.drawable.ic_news,
        filledIcon = R.drawable.ic_news_filled,
        label = "News"
    )

    object Favorites : BottomBarItem(
        graphRoute = "favorites_graph",
        startRoute = "favorites",
        icon = R.drawable.ic_bookmark,
        filledIcon = R.drawable.ic_bookmark_filled,
        label = "Favs"
    )
}