package com.solmi.biobrainexample.kotlin.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.solmi.biobrainexample.R

class HomeKtActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_kt)

        initNavBinding()

    }

    /**
     * Navigation Binding
     * 2022.04.23
     */
    private fun initNavBinding() {

        //navigation Binding
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(navController)
    }
}