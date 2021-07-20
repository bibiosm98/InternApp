package com.example.internapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.internapp.databinding.SplashFragmentBinding
import com.example.internapp.ui.SplashFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_InternApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.navigation_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val fragmentsWithList = listOf(
            R.id.loginFragment,
            R.id.registrationFragment,
            R.id.profileFragment,
            R.id.splashFragment
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                in fragmentsWithList -> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.splashFragment, R.id.loginFragment, R.id.homeFragment, R.id.searchFragment
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}