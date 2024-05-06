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
import com.example.parking.data.db.users.User
import com.example.parking.data.db.users.UserInfo
import com.example.parking.data.db.users.UsersEntity
import com.example.parking.databinding.FragmentRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }



    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registerUsername = binding.nickname
        val registerName = binding.name
        val registerSurname = binding.firstSurname
        val registerEmail = binding.email
        val registerPassword = binding.password
        val registerRol = binding.rol

        binding.registerButton.setOnClickListener {
            val username = registerUsername.text.toString()
            val name = registerName.text.toString()
            val surname = registerSurname.text.toString()
            val email = registerEmail.text.toString()
            val password = registerPassword.text.toString()
            val rol = registerRol.text.toString()

            val id = 2
            val uuid = "2"
            val picture = "algo"

            val userData = UsersEntity(id, uuid, username, name, picture, surname, email, password, rol)

            registerUser(userData)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun registerUser(userData: UsersEntity) {
        lifecycleScope.launch {
            auth = Firebase.auth
            auth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val userInfo = UserInfo(userData.uuid, userData.username, userData.name, userData.picture, userData.surname, userData.email, userData.role)
                        writeNewUser(userInfo)
                        val user = auth.currentUser
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        requireActivity().startActivity(intent)
                        requireActivity().finish()
                    } else {
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

    fun writeNewUser(userData: UserInfo) {
        val user = UserInfo(userData.uuid, userData.username, userData.name, userData.picture, userData.surname, userData.email, userData.role)
        firestore = Firebase.firestore
        firestore.collection("userInfo")
            .add(user)
            .addOnSuccessListener { documentReference ->
                val uuid = documentReference.id
                user.uuid = uuid
                documentReference.update("uuid", uuid)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully written!")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error writing document", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}
