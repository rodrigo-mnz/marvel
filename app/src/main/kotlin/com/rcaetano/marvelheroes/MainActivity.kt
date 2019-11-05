package com.rcaetano.marvelheroes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rcaetano.marvelheroes.feature.home.HomeFragment

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


    }
}
