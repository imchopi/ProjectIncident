package com.example.parking.ui.register

import android.content.Intent
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.parking.MainActivity
import com.example.parking.data.api.ParkingService
import com.example.parking.data.db.users.User
import com.example.parking.data.db.users.UsersEntity
import com.example.parking.data.repository.Repository
import com.example.parking.databinding.FragmentRegisterBinding
import com.example.parking.ui.login.LoginFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.Credentials
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: LoginFragmentViewModel by viewModels()

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
        binding.registerButton.setOnClickListener {
            val nickname = binding.nickname.text.toString()
            val name = binding.name.text.toString()
            val firstSurname = binding.firstSurname.text.toString()
            val secondSurname = binding.secondSurname.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val rol = binding.rol.text.toString()

            val id = 2
            val userData = UsersEntity(id, nickname, name, firstSurname, secondSurname, email, password, rol)

            registerUser(userData)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun registerUser(userData: UsersEntity) {
        lifecycleScope.launch {
            try {
                val credentials = Credentials.basic("nuevo@gmail.com", "Admin123")
                viewModel.register(userData)
                /*repository.addUser(userData)*/
                val intent = Intent(requireActivity(), MainActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            } catch (e: HttpException) {
                Log.e("Error", "Error al registrarse: ${e.message}")
            }
        }
    }
}
