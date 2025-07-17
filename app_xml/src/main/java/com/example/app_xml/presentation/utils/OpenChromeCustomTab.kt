package com.example.app_xml.presentation.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.example.app_xml.R

fun openChromeCustomTab(context: Context, url: String) {
    val backIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_arrow_left)

    val customTabsIntent = CustomTabsIntent.Builder()
        .setCloseButtonIcon(backIcon)
        .setShowTitle(true)
        .build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}