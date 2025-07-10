package com.example.app_xml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.app_xml.databinding.ActivityMainBinding
import com.example.app_xml.presentation.favorites.FavoritesFragment
import com.example.app_xml.presentation.newsfeed.NewsfeedFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding MainActivity null")

    private val newsFragment = NewsfeedFragment()
    private val favoritesFragment = FavoritesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, newsFragment)
                .commit()
        }

        binding.bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuNewsfeed -> {
                    openFragment(newsFragment)
                    true
                }
                R.id.menuFavorites -> {
                    openFragment(favoritesFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}