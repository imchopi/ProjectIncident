package com.example.parking.ui.incident.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.parking.MainActivity
import com.example.parking.R
import com.example.parking.data.api.ParkingService
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.data.db.users.UsersEntity
import com.example.parking.databinding.FragmentAddIncidentBinding
import com.example.parking.databinding.FragmentHomeBinding
import com.example.parking.ui.home.HomeFragmentDirections
import com.example.parking.ui.login.LoginFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.Credentials
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class AddIncidentFragment : Fragment() {

    private var selectedImageUri: Uri? = null
    private val viewModelLogin: LoginFragmentViewModel by viewModels()
    private lateinit var binding: FragmentAddIncidentBinding
    private lateinit var optionsContainer: LinearLayout
    private lateinit var cameraButton: Button
    private lateinit var galleryButton: Button
    private val viewModel: AddIncidentViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddIncidentBinding.inflate(inflater, container, false)

        optionsContainer = binding.optionsContainer.findViewById(R.id.optionsContainer)
        cameraButton = binding.cameraButton.findViewById(R.id.cameraButton)
        galleryButton = binding.galleryButton.findViewById(R.id.galleryButton)

        val optionsButton: ImageButton = binding.optionsButton.findViewById(R.id.optionsButton)
        optionsButton.setOnClickListener {
            toggleOptionsVisibility()
        }

        val imageView = binding.imageView

        // Observar cambios en el StateFlow y actualizar el ImageView
        lifecycleScope.launch {
            viewModel.photoUri.collect { uri ->
                // Actualizar el ImageView con la nueva URI
                uri?.let {
                    imageView?.setImageURI(uri)
                }
            }
        }

        return binding.root
    }

    private fun toggleOptionsVisibility() {
        if (optionsContainer.visibility == View.VISIBLE) {
            optionsContainer.visibility = View.INVISIBLE
        } else {
            optionsContainer.visibility = View.VISIBLE
        }
    }
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            val credentials = Credentials.basic("nuevo@gmail.com", "Admin123")
            val title = binding.titleEditText.text.toString()
            val description = binding.textEditText.text.toString()
            val image = binding.imageView.setImageURI(selectedImageUri).toString()
            Log.d("Foto", "Foto $image")
            val incident = IncidentsEntity(2, title, description, "", 1, image)
            registerIncident(incident)
            findNavController().navigate(AddIncidentFragmentDirections.actionAddIncidentFragmentToHome())
        }
        binding.galleryButton.setOnClickListener {
            requestPermission()
        }
        binding.cameraButton.setOnClickListener {
            findNavController().navigate(AddIncidentFragmentDirections.actionAddIncidentFragmentToCameraFragment())
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun registerIncident(incident: IncidentsEntity) {
        lifecycleScope.launch {
            try {
                val credentials = Credentials.basic("nuevo@gmail.com", "Admin123")
                viewModelLogin.addIncident(incident)
                val intent = Intent(requireActivity(), MainActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            } catch (e: HttpException) {
                Log.e("Error", "Error al registrarse: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickPhotoFromGallery()
                }
                else -> requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            pickPhotoFromGallery()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForFragmentGallery.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val startForFragmentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data
            selectedImageUri = data
            binding.imageView.setImageURI(data)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {isGranted ->
        if (isGranted){
            pickPhotoFromGallery()
        }else {
            Toast.makeText(requireContext(),"algo", Toast.LENGTH_SHORT).show()
        }

    }
}