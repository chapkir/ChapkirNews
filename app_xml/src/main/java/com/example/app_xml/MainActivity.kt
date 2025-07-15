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

    private var activeFragment: Fragment = newsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, favoritesFragment, "Favorites")
                .hide(favoritesFragment)
                .add(R.id.fragmentContainer, newsFragment, "News")
                .commit()
        } else {
            activeFragment = supportFragmentManager.findFragmentByTag("News") ?: newsFragment
        }

        binding.bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuNewsfeed -> {
                    switchFragment(newsFragment)
                    true
                }

                R.id.menuFavorites -> {
                    switchFragment(favoritesFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun switchFragment(targetFragment: Fragment) {
        if (activeFragment == targetFragment) return

        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(targetFragment)
            .commit()

        activeFragment = targetFragment
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}