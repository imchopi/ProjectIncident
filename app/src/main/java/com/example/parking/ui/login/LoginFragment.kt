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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.parking.MainActivity
import com.example.parking.data.api.UserLogin
import com.example.parking.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.Credentials
import retrofit2.HttpException

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: LoginFragmentViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)

        val loginUsernameEditText = binding.loginUsername
        val loginPasswordEditText = binding.loginPassword

        binding.registerLogin.setOnClickListener {
            val username = loginUsernameEditText.text.toString()
            val password = loginPasswordEditText.text.toString()
            login(username, password)
        }

        return binding.root
    }


    private fun login(username: String, password: String) {
        if (username.isBlank()) {
            // Usuario no proporcionado, muestra un mensaje de error
            Toast.makeText(
                requireContext(),
                "Por favor, introduce un nombre de usuario",
                Toast.LENGTH_SHORT
            ).show()
            return // Salir de la función sin intentar iniciar sesión
        }

        if (password.isBlank()) {
            // Contraseña no proporcionada, muestra un mensaje de error
            Toast.makeText(
                requireContext(),
                "Por favor, introduce una contraseña",
                Toast.LENGTH_SHORT
            ).show()
            return // Salir de la función sin intentar iniciar sesión
        }

        lifecycleScope.launch {
            auth = Firebase.auth
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        requireActivity().startActivity(intent)
                        requireActivity().finish()
                    } else {
                        // If sign in fails, display a message to the user.
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