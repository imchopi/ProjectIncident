package com.example.parking.ui.register

import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.parking.data.api.ParkingService
import com.example.parking.data.db.users.User
import com.example.parking.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.registerButton.setOnClickListener {
            val nickname = binding.nickname.text.toString()
            val name = binding.name.text.toString()
            val firstSurname = binding.firstSurname.text.toString()
            val secondSurname = binding.secondSurname.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val rol = binding.rol.text.toString()

            val id = 0
            val userData = User(id, nickname, name, firstSurname, secondSurname, email, password, rol)

            /*registerUser(userData)*/
        }

        return binding.root
    }

    /*@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun registerUser(userData: User) {
        lifecycleScope.launch {
            try {
                val user = ParkingService().api.registerUser(userData)
            } catch (e: HttpException) {
                Log.e("Error", "Error al registrarse: ${e.message}")
            }
        }
    }*/
}
