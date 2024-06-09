package com.example.parking

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.parking.databinding.ActivityLoginBinding
import com.example.parking.ui.incident.add.camera.CameraFragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

// An activity responsible for handling login and registration
@AndroidEntryPoint
/**
 * Activity for handling login functionality.
 */
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

        // Request necessary permissions
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

    }

    /**
     * Check if all required permissions are granted.
     *
     * @return True if all permissions are granted, false otherwise.
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it) == PackageManager.PERMISSION_GRANTED
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this,
                    getString(R.string.permissions),
                    Toast.LENGTH_SHORT).show()
            }
        }
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

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                // Add WRITE_EXTERNAL_STORAGE permission for devices with SDK <= P
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

}
