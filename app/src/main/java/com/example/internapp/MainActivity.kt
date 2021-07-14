package com.example.internapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.navigation_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val fragmentsWithList = listOf(R.id.loginFragment, R.id.registrationFragment, R.id.profileFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                in fragmentsWithList -> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.loginFragment, R.id.homeFragment
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