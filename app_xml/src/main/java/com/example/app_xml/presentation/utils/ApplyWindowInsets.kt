package com.example.app_xml.presentation.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentActivity

/**
 * @param activity - текущая Activity
 * @param targetView - View, к которому нужно применить отступ
 * @param insetTypes - Типы Insets (например, ime(), navigationBars(), statusBars())
 */

fun applyWindowInsets(
    activity: FragmentActivity,
    targetView: View,
    insetTypes: Int,
    applyTop: Boolean = false,
    applyBottom: Boolean = false,
) {
    WindowCompat.setDecorFitsSystemWindows(activity.window, false)

    ViewCompat.setOnApplyWindowInsetsListener(targetView) { view, insets ->
        val appliedInsets = insets.getInsets(insetTypes)

        view.updatePadding(
            top = if (applyTop) appliedInsets.top else view.paddingTop,
            bottom = if (applyBottom) appliedInsets.bottom else view.paddingBottom
        )

        insets
    }

    ViewCompat.requestApplyInsets(targetView)
}