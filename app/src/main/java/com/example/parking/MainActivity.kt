package com.example.parking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.parking.databinding.ActivityMainBinding
import com.example.parking.ui.home.HomeFragmentDirections
import com.example.parking.ui.incident.add.camera.CameraFragment
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
/**
 * Main activity that serves as the entry point of the application after login.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        // Initialize navigation components
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navViewMain
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Define the top-level destinations for the app bar configuration
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home, R.id.addIncidentFragment
            ), drawerLayout
        )

        // Setup the action bar with navigation controller and app bar configuration
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Setup the navigation view with navigation controller
        navView.setupWithNavController(navController)

        // Handle navigation item clicks in the navigation view
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    logout()
                    true
                }
                R.id.addIncidentFragment -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.addIncidentFragment)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.home -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.home)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }

        // Update navigation header with user information
        updateNavHeader()
    }

    /**
     * Update the navigation drawer header with user information.
     */
    private fun updateNavHeader() {
        auth = Firebase.auth
        val navView: NavigationView = binding.navViewMain
        val headerView: View = navView.getHeaderView(0)
        val emailTextView: TextView = headerView.findViewById(R.id.emailTextView)
        val imageView: ImageView = headerView.findViewById(R.id.imageView)

        val user = auth.currentUser
        user?.let { currentUser ->
            val email = currentUser.email ?: "user@example.com"
            emailTextView.text = email

            // Initialize Firestore
            val db = Firebase.firestore

            // Query Firestore to get user info
            val userInfoRef = db.collection("userInfo").document(currentUser.uid)
            userInfoRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val data = document.data
                        val pictureUrl = data?.get("picture") as? String
                        if (pictureUrl != null) {
                            // Load the image using Glide
                            Glide.with(this)
                                .load(pictureUrl)
                                .placeholder(R.drawable.ic_launcher_foreground) // optional placeholder
                                .into(imageView)
                        } else {
                            // Handle the case where picture URL is null
                            Glide.with(this)
                                .load(R.drawable.ic_launcher_foreground) // optional placeholder
                                .into(imageView)
                        }
                    } else {
                        // Handle the case where document does not exist
                        Log.d("Firestore", getString(R.string.noSuchDocument))
                        Glide.with(this)
                            .load(R.drawable.ic_launcher_foreground) // optional placeholder
                            .into(imageView)
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.w("Firestore", getString(R.string.gettingDocument), exception)
                    Glide.with(this)
                        .load(R.drawable.ic_launcher_foreground) // optional placeholder
                        .into(imageView)
                }
        }
    }

    /**
     * Logout the current user and navigate to the login screen.
     */
    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // Initialize the contents of the Activity's standard options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    // Handle up navigation in the app bar
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
