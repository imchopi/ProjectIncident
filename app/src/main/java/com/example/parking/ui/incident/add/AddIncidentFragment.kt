package com.example.parking.ui.incident.add

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.parking.MainActivity
import com.example.parking.R
import com.example.parking.data.db.incidents.Incident
import com.example.parking.databinding.FragmentAddIncidentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Date
import java.util.UUID

@AndroidEntryPoint
class AddIncidentFragment : Fragment() {

    // Firebase Firestore instance
    private lateinit var firestore: FirebaseFirestore
    private lateinit var category: Spinner

    // Selected image URI
    private var selectedImageUri: Uri? = null

    // View binding for the fragment layout
    private lateinit var binding: FragmentAddIncidentBinding

    // UI components
    private lateinit var optionsContainer: LinearLayout
    private lateinit var cameraButton: Button
    private lateinit var galleryButton: Button

    // View model
    private val viewModel: AddIncidentViewModel by activityViewModels()

    // Called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Called to create the fragment's view hierarchy
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddIncidentBinding.inflate(inflater, container, false)

        // Initialize UI components
        optionsContainer = binding.optionsContainer.findViewById(R.id.optionsContainer)
        cameraButton = binding.cameraButton.findViewById(R.id.cameraButton)
        galleryButton = binding.galleryButton.findViewById(R.id.galleryButton)
        category = binding.category

        // Set click listener for options button
        val optionsButton: ImageButton = binding.optionsButton.findViewById(R.id.optionsButton)
        optionsButton.setOnClickListener {
            toggleOptionsVisibility()
        }

        // Observe photo URI changes and update image view accordingly
        val imageView = binding.imageView
        lifecycleScope.launch {
            viewModel.photoUri.collect { uri ->
                uri?.let {
                    imageView?.setImageURI(uri)
                }
            }
        }
        return binding.root
    }

    // Function to toggle visibility of options container
    private fun toggleOptionsVisibility() {
        if (optionsContainer.visibility == View.VISIBLE) {
            optionsContainer.visibility = View.INVISIBLE
        } else {
            optionsContainer.visibility = View.VISIBLE
        }
    }

    // Called when the fragment's view has been created
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize UI components and set click listeners
        val spinner = binding.category
        val addTitle = binding.titleEditText
        val addDescription = binding.textEditText
        val addImage = binding.imageView

        setupCategorySpinner()

        binding.button.setOnClickListener {
            val title = addTitle.text.toString()
            val description = addDescription.text.toString()
            val image = addImage.setImageURI(selectedImageUri).toString()
            Log.d("Foto", "Foto $image")
            val uuid = "2"
            val date = Date().toString()
            val category = spinner.selectedItem as String

            // Create an Incident object
            val incident = Incident(
                uuid,
                category,
                title,
                description,
                image,
                date,
                false,
                false,
                ""
            )

            // Register the incident
            registerIncident(incident)
        }

        // Set click listeners for gallery and camera buttons
        binding.galleryButton.setOnClickListener {
            requestPermission()
        }
        binding.cameraButton.setOnClickListener {
            findNavController().navigate(AddIncidentFragmentDirections.actionAddIncidentFragmentToCameraFragment())
        }
    }

    // Function to register an incident
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun registerIncident(incidentInfo: Incident) {
        if (incidentInfo.title.isBlank() || incidentInfo.description.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Please fill in all fields",
                Toast.LENGTH_SHORT
            ).show()
            return
        } else {
            lifecycleScope.launch {
                try {
                    firestore = Firebase.firestore
                    val firebaseAuth = FirebaseAuth.getInstance()
                    val currentUser = firebaseAuth.currentUser
                    val uid = currentUser?.uid ?: ""
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("images/${UUID.randomUUID()}")

                    selectedImageUri?.let { uri ->
                        val inputStream = requireContext().contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val outputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                        val imageData = outputStream.toByteArray()
                        imageRef.putBytes(imageData)
                            .addOnSuccessListener { uploadTask ->
                                uploadTask.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                                    val imageUrl = downloadUri.toString()
                                    incidentInfo.image = imageUrl
                                    val incident = Incident(
                                        incidentInfo.uuid,
                                        incidentInfo.categoryName,
                                        incidentInfo.title,
                                        incidentInfo.description,
                                        imageUrl,
                                        incidentInfo.date,
                                        incidentInfo.checked,
                                        incidentInfo.resolved,
                                        uid,
                                    )
                                    firestore.collection("incidentsInfo")
                                        .add(incident)
                                        .addOnSuccessListener { documentReference ->
                                            val uuid = documentReference.id
                                            incident.uuid = uuid
                                            documentReference.update("uuid", uuid)
                                                .addOnSuccessListener {
                                                    Log.d(
                                                        ContentValues.TAG,
                                                        "DocumentSnapshot successfully written!"
                                                    )
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.w(
                                                        ContentValues.TAG,
                                                        "Error writing document",
                                                        e
                                                    )
                                                }
                                        }
                                        .addOnCompleteListener {
                                            val intent = Intent(requireActivity(), MainActivity::class.java)
                                            requireActivity().startActivity(intent)
                                            requireActivity().finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(ContentValues.TAG, "Error adding document", e)
                                        }
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("Error", "Error uploading image: ${e.message}")
                            }
                    } ?: run {
                        Toast.makeText(
                            requireContext(),
                            "Please select an image",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: HttpException) {
                    Log.e("Error", "Error registering incident: ${e.message}")
                }
            }
        }
    }

    private fun setupCategorySpinner() {
        firestore = Firebase.firestore
        firestore.collection("categoryInfo")
            .get()
            .addOnSuccessListener { documents ->
                val categoryNames = documents.map { it.getString("name") ?: "Unnamed" }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                category.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error getting categories", e)
            }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun requestPermission() {
        // Check if the device's SDK version is greater than or equal to Marshmallow (API 23)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if permission to read external storage is already granted
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // If permission is already granted, pick a photo from the gallery
                    pickPhotoFromGallery()
                }
                else -> {
                    // If permission is not granted, request permission using the permission launcher
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        } else {
            // If device's SDK version is lower than Marshmallow, directly pick a photo from the gallery
            pickPhotoFromGallery()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun pickPhotoFromGallery() {
        // Create an intent to pick an image from the gallery
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        // Launch the activity for result using ActivityResultContracts
        startForFragmentGallery.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val startForFragmentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result of picking an image from the gallery
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            selectedImageUri = data
            // Set the selected image URI to the image view
            binding.imageView.setImageURI(data)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Handle the result of the permission request for gallery access
        if (isGranted) {
            // If permission is granted, pick photo from the gallery
            pickPhotoFromGallery()
        } else {
            // If permission is denied, show a toast message
            Toast.makeText(requireContext(), "algo", Toast.LENGTH_SHORT).show()
        }
    }
}