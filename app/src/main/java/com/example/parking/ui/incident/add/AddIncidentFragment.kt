package com.example.parking.ui.incident.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.parking.R
import com.example.parking.databinding.FragmentAddIncidentBinding
import com.example.parking.databinding.FragmentHomeBinding
import com.example.parking.ui.home.HomeFragmentDirections
import kotlinx.coroutines.launch

class AddIncidentFragment : Fragment() {

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
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.galleryButton.setOnClickListener {
            requestPermission()
        }
        binding.cameraButton.setOnClickListener {
            findNavController().navigate(AddIncidentFragmentDirections.actionAddIncidentFragmentToCameraFragment())
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