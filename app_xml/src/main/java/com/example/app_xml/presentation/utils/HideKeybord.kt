package com.example.app_xml.presentation.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusView = requireActivity().currentFocus ?: View(requireContext())
    inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
}