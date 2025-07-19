package com.example.app_xml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.app_xml.databinding.ActivityMainBinding
import com.example.app_xml.presentation.newsfeed.NewsfeedFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding MainActivity null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                    as NavHostFragment).navController

        binding.bottomNavBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newsfeedFragment ->
                    binding.bottomNavBar.menu.findItem(R.id.newsfeedFragment).isChecked = true

                R.id.favoritesFragment ->
                    binding.bottomNavBar.menu.findItem(R.id.favoritesFragment).isChecked = true

                else -> binding.bottomNavBar.clearFocus()
            }
        }

        binding.bottomNavBar.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.newsfeedFragment -> {
                    val navHostFragment = supportFragmentManager
                        .findFragmentById(R.id.fragmentContainer) as? NavHostFragment
                    val currentFragment = navHostFragment
                        ?.childFragmentManager
                        ?.fragments
                        ?.firstOrNull()

                    if (currentFragment is NewsfeedFragment) {
                        currentFragment.scrollToTopAndRefresh()
                    }
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavBar) { view, insets ->
            view.updatePadding(
                bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            )
            WindowInsetsCompat.Builder(insets)
                .setInsets(WindowInsetsCompat.Type.ime(), Insets.NONE)
                .build()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}