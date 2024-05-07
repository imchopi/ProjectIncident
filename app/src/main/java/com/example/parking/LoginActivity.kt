package com.example.parking

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.parking.databinding.ActivityLoginBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

// An activity responsible for handling login and registration
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    // Called when the activity is starting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set the toolbar as the action bar
        setSupportActionBar(binding.appBarLogin.toolbar)

        // Initialize navigation components
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navViewLogin
        val navController = findNavController(R.id.nav_host_fragment_content_login)
        // Define the top-level destinations for the app bar configuration
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.login, R.id.register
            ), drawerLayout
        )
        // Setup the action bar with navigation controller and app bar configuration
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Setup the navigation view with navigation controller
        navView.setupWithNavController(navController)
    }

    // Initialize the contents of the Activity's standard options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    // Handle up navigation in the app bar
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_login)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}