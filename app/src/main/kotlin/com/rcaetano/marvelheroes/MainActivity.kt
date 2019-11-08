package com.rcaetano.marvelheroes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rcaetano.marvelheroes.feature.favourite.FavouriteFragment
import com.rcaetano.marvelheroes.feature.home.HomeFragment
import com.rcaetano.marvelheroes.feature.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val home = HomeFragment()
    private val search: SearchFragment by lazy { SearchFragment() }
    private val favourite: FavouriteFragment by lazy { FavouriteFragment() }
    private var current: Fragment = home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.fragment_container,
                home,
                HomeFragment::class.java.simpleName
            )
            .add(R.id.fragment_container, search)
            .hide(search)
            .add(R.id.fragment_container, favourite)
            .hide(favourite)
            .commit()

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->

            val fragment = when (menuItem.itemId) {
                R.id.item_home -> home
                R.id.item_search -> search
                R.id.item_favourite -> {
                    favourite.loadList()
                    favourite
                }
                else -> home
            }

            supportFragmentManager
                .beginTransaction()
                .show(fragment)
                .hide(current)
                .commit()

            current = fragment

            true
        }
    }
}
