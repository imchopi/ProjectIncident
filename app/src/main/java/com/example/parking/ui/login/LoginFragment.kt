package com.example.parking.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.parking.MainActivity
import com.example.parking.data.api.UserLogin
import com.example.parking.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.Credentials
import retrofit2.HttpException

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginFragmentViewModel by viewModels()


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
                val credentialesUser = UserLogin(username, password)
                viewModel.userLogin(credentialesUser)
                val logged = viewModel.login(authHeader)
                if (logged != null) {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                } else {

                }



            } catch (e: HttpException) {
                Log.e("Error","Error al obtener el CSRF token: ${e.response()?.errorBody()?.string()}")
            } catch (e: HttpException) {
                Log.e("Error", "Error al obtener el CSRF token: ${e.response()?.errorBody()?.string()}")
            }
        }
    }

}