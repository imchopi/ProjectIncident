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
import com.example.parking.R
import com.example.parking.data.db.users.User
import com.example.parking.data.db.users.UserInfo
/*import com.example.parking.data.db.users.UserInfo
import com.example.parking.data.db.users.UsersEntity*/
import com.example.parking.databinding.FragmentRegisterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID

// Fragment for user registration
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    // View binding instance
    private lateinit var binding: FragmentRegisterBinding

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Firebase Firestore instance
    private lateinit var firestore: FirebaseFirestore

    /**
     * Called to create the fragment's view hierarchy.
     *
     * @param inflater The layout inflater object that can be used to inflate any views in the fragment.
     * @param container This is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState This fragment is being re-constructed from a previous saved state as given here.
     * @return Returns the View for the fragment's UI, or null.
     */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called after the fragment's view is created.
     *
     * @param view The fragment's root view.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
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
                Snackbar.make(requireView(), getString(R.string.error_fill_fields), Snackbar.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(getString(R.string.error_password_mismatch))
                    .setPositiveButton(getString(R.string.dialog_error_positive_button)) { dialog, which ->
                        // Respond to positive button press
                        dialog.dismiss()
                    }
                    .show()
            } else {
                // Create a new user data object
                val uuid = UUID.randomUUID().toString()
                val picture = "" // You can set the picture here if required
                val userData = User(uuid, username, name, picture, surname, email, password, "user")

                // Register the user
                registerUser(userData)
            }
        }
    }

    /**
     * Attempt to register a new user with the provided user data.
     *
     * @param userData The user data object containing the username, name, surname, email, password, etc.
     */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun registerUser(userData: User) {
        lifecycleScope.launch {
            // Initialize Firebase Authentication
            auth = Firebase.auth
            // Create user with email and password
            auth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Registration successful
                        Log.d(TAG,getString(R.string.createUser) )
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
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.authentication),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

    /**
     * Write the user data to Firestore after successful user registration.
     *
     * @param userData The user data object containing the username, name, surname, email, password, etc.
     */
    private fun writeNewUser(userData: UserInfo) {
        // Initialize Firebase Firestore
        firestore = Firebase.firestore

        // Use the Firebase Auth UID as the document ID
        val documentReference = firestore.collection("userInfo").document(userData.uuid)

        // Set the document data
        documentReference.set(userData)
            .addOnSuccessListener {

                Log.d(TAG, getString(R.string.snapshot))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, getString(R.string.errorDocument), e)
            }
    }

    companion object {
        private const val TAG = "RegisterFragment"
    }
}
