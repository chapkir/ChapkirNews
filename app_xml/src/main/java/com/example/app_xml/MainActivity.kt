package com.example.app_xml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.app_xml.databinding.ActivityMainBinding
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

        val navController =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainer)
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}