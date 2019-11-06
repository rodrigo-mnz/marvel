package com.rcaetano.marvelheroes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rcaetano.marvelheroes.feature.home.HomeFragment
import com.rcaetano.marvelheroes.feature.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.fragment_container,
                HomeFragment(),
                HomeFragment::class.java.simpleName
            )
            .commit()

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->

            val fragment = when (menuItem.itemId) {
                R.id.item_home -> supportFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
                    ?: HomeFragment()

                R.id.item_favorite -> null

                R.id.item_search -> supportFragmentManager.findFragmentByTag(SearchFragment::class.java.simpleName)
                    ?: SearchFragment()

                else -> null
            }

            fragment?.let {
                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        fragment,
                        fragment::class.java.simpleName
                    )
                    .addToBackStack(fragment::class.java.simpleName)
                    .commit()
            }

            true
        }
    }
}
