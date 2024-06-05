package com.example.parking.ui.register

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.parking.MainActivity
import com.example.parking.data.db.users.UserInfo
import com.example.parking.data.db.users.UsersEntity
import com.example.parking.databinding.FragmentRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Fragment for user registration
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    // View binding instance
    private lateinit var binding: FragmentRegisterBinding

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Firebase Firestore instance
    private lateinit var firestore: FirebaseFirestore

    // Called to create the fragment's view hierarchy
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Called after the fragment's view is created
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components
        val registerUsername = binding.nickname
        val registerName = binding.name
        val registerSurname = binding.firstSurname
        val registerEmail = binding.email
        val registerPassword = binding.password
        val registerConfirmPassword = binding.confirmPassword

        // Set click listener for register button
        binding.registerButton.setOnClickListener {
            val username = registerUsername.text.toString()
            val name = registerName.text.toString()
            val surname = registerSurname.text.toString()
            val email = registerEmail.text.toString()
            val password = registerPassword.text.toString()
            val confirmPassword = registerConfirmPassword.text.toString()

            // Check if all fields are filled
            if (username.isEmpty() || name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Use the same password", Toast.LENGTH_SHORT).show()
            } else {
                // Create a new user data object
                val uuid = "2" // Sample UUID
                val picture = "algo" // Sample picture
                val userData = UsersEntity(id, uuid, username, name, picture, surname, email, password, "user")

                // Register the user
                registerUser(userData)
            }
        }
    }

    // Function to register a new user
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun registerUser(userData: UsersEntity) {
        lifecycleScope.launch {
            // Initialize Firebase Authentication
            auth = Firebase.auth
            // Create user with email and password
            auth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Registration successful
                        Log.d(TAG, "createUserWithEmail:success")
                        // Get the Firebase Auth UID
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            // Create user info object
                            val userInfo = UserInfo(
                                uid, // Use Firebase Auth UID here
                                userData.username,
                                userData.name,
                                userData.picture,
                                userData.surname,
                                userData.email,
                                userData.role
                            )
                            // Write user data to Firestore
                            writeNewUser(userInfo)
                        }

                        // Navigate to MainActivity upon successful registration
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        requireActivity().startActivity(intent)
                        requireActivity().finish()
                    } else {
                        // Registration failed
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

    // Function to write user data to Firestore
    fun writeNewUser(userData: UserInfo) {
        // Initialize Firebase Firestore
        val firestore = Firebase.firestore

        // Use the Firebase Auth UID as the document ID
        val documentReference = firestore.collection("userInfo").document(userData.uuid)

        // Set the document data
        documentReference.set(userData)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
            }
    }
}
