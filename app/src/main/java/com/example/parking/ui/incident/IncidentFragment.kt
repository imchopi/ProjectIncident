package com.example.parking.ui.incident

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.parking.databinding.FragmentIncidentBinding

class IncidentFragment : Fragment() {

    private lateinit var binding: FragmentIncidentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIncidentBinding.inflate(inflater,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addIncident.setOnClickListener {
            findNavController().navigate(IncidentFragmentDirections.actionIncidentFragmentToAddIncidentFragment())
        }
    }

}