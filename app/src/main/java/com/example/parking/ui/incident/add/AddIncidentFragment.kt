package com.example.parking.ui.incident.add

import android.Manifest
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
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
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
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.databinding.FragmentAddIncidentBinding
import com.example.parking.ui.login.LoginFragmentViewModel
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

    private lateinit var firestore: FirebaseFirestore
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
            val title = binding.titleEditText.text.toString()
            val description = binding.textEditText.text.toString()
            val image = binding.imageView.setImageURI(selectedImageUri).toString()
            Log.d("Foto", "Foto $image")
            val uuid = "2"
            val date = Date().toString()

            val incident = Incident(
                uuid,
                "Network",
                title,
                description,
                image,
                date,
                false,
                false,
                "" )
            registerIncident(incident)
        }
        binding.galleryButton.setOnClickListener {
            requestPermission()
        }
        binding.cameraButton.setOnClickListener {
            findNavController().navigate(AddIncidentFragmentDirections.actionAddIncidentFragmentToCameraFragment())
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun registerIncident(incidentInfo: Incident) {
        if (incidentInfo.title.isBlank() || incidentInfo.description.isBlank()) {
            // Mostrar un mensaje de error si alguno de los campos está vacío
            Toast.makeText(
                requireContext(),
                "Por favor, completa todos los campos",
                Toast.LENGTH_SHORT
            ).show()
            return // Salir de la función sin registrar el incidente
        } else {
            lifecycleScope.launch {
                try {
                    firestore = Firebase.firestore
                    val firebaseAuth = FirebaseAuth.getInstance()
                    val currentUser = firebaseAuth.currentUser
                    val uid = currentUser?.uid ?: ""
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("images/${UUID.randomUUID()}")
                    // Comprobar si hay una imagen seleccionada
                    selectedImageUri?.let { uri ->
                        val inputStream = requireContext().contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val outputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream) // Ajusta la calidad según necesites (0-100)
                        val imageData = outputStream.toByteArray()
                        imageRef.putBytes(imageData)
                            .addOnSuccessListener { uploadTask ->
                                // Obtener la URL de descarga de la imagen
                                uploadTask.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                                    // Actualizar la URL de la imagen en el objeto del incidente
                                    val imageUrl = downloadUri.toString()
                                    incidentInfo.image = imageUrl

                                    // Crear el objeto Incident con la URL de la imagen actualizada
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

                                    // Guardar el incidente en Firestore
                                    firestore = Firebase.firestore
                                    firestore.collection("incidentsInfo")
                                        .add(incident)
                                        .addOnSuccessListener { documentReference ->
                                            val uuid = documentReference.id
                                            incident.uuid = uuid
                                            documentReference.update("uuid", uuid)
                                                .addOnSuccessListener {
                                                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.w(ContentValues.TAG, "Error writing document", e)
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
                                Log.e("Error", "Error al cargar la imagen: ${e.message}")
                            }
                    } ?: run {
                        // Si no hay ninguna imagen seleccionada, mostrar un mensaje de error
                        Toast.makeText(
                            requireContext(),
                            "Por favor, selecciona una imagen",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: HttpException) {
                    Log.e("Error", "Error al registrarse: ${e.message}")
                }
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