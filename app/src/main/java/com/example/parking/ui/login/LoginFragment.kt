package com.example.parking.ui.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.parking.MainActivity
import com.example.parking.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Fragment for user login
@AndroidEntryPoint
class LoginFragment : Fragment() {

    // View binding instance
    private lateinit var binding: FragmentLoginBinding

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Called to create the fragment's view hierarchy
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        binding = FragmentLoginBinding.inflate(inflater,container,false)

        // Initialize UI components
        val loginUsernameEditText = binding.loginUsername
        val loginPasswordEditText = binding.loginPassword

        // Set click listener for login button
        binding.registerLogin.setOnClickListener {
            val username = loginUsernameEditText.text.toString()
            val password = loginPasswordEditText.text.toString()
            // Call login function
            login(username, password)
        }

        return binding.root
    }

    // Function to handle user login
    private fun login(username: String, password: String) {
        // Check if username is blank
        if (username.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Please enter a username",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Check if password is blank
        if (password.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Please enter a password",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Perform login operation using Firebase Authentication
        lifecycleScope.launch {
            auth = Firebase.auth
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Login successful
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        // Navigate to MainActivity upon successful login
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        requireActivity().startActivity(intent)
                        requireActivity().finish()
                    } else {
                        // Login failed
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}
