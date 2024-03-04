package com.example.parking.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.parking.MainActivity
import com.example.parking.R
import com.example.parking.data.api.ParkingService
import com.example.parking.databinding.ActivityMainBinding
import com.example.parking.databinding.FragmentLoginBinding
import com.example.parking.di.ParkingApplication
import com.example.parking.interfaces.Csrf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import retrofit2.HttpException

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)

        binding.registerLogin.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()
            initCsrfToken(username, password)
        }

        return binding.root
    }



    private fun initCsrfToken(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val authHeader = Credentials.basic(username, password)

                val service = ParkingService(authHeader)
                val csrfToken = service.api.getCsrf().token

                val intent = Intent(requireActivity(), MainActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()

                Log.d("Token", "Este es el token: $csrfToken")
            } catch (e: HttpException) {
                Log.e("Error","Error al obtener el CSRF token: ${e.response()?.errorBody()?.string()}")
            } catch (e: HttpException) {
                Log.e("Error", "Error al obtener el CSRF token: ${e.response()?.errorBody()?.string()}")
            }
        }
    }

}