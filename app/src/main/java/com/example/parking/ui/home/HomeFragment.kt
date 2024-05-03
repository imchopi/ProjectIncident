package com.example.parking.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.parking.data.db.incidents.Incident
import com.example.parking.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HomeAdapter(requireContext(), ::toDetail)
        val recyclerView = binding.incidents
        recyclerView.adapter = adapter

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        userId?.let { userId ->
            firestore = Firebase.firestore
            firestore.collection("incidentsInfo")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val incidentsList = mutableListOf<Incident>()
                    for (document in querySnapshot) {
                        val data = document.data
                        val uuid = data["uuid"] as? String ?: ""
                        val categoryName = data["categoryName"] as? String ?: ""
                        val title = data["title"] as? String ?: ""
                        val description = data["description"] as? String ?: ""
                        val image = data["image"] as? String
                        val date = data["date"]?.toString() ?: ""
                        val checked = data["checked"] as? Boolean ?: false
                        val resolved = data["resolved"] as? Boolean ?: false

                        val incident = Incident(
                            uuid = uuid,
                            categoryName = categoryName,
                            title = title,
                            description = description,
                            image = image,
                            date = date,
                            checked = checked,
                            resolved = resolved,
                            userId = userId
                        )
                        incidentsList.add(incident)
                    }
                    adapter.submitList(incidentsList)
                }.addOnFailureListener { exception ->
                    Log.e("Firestore", "Error fetching documents: $exception")
                }
        }
    }

    fun toDetail (incident: Incident) {
        val action = incident.uuid?.let { HomeFragmentDirections.actionHomeToDetailFragment(it) }
        if (action != null) {
            findNavController().navigate(action)
        }
    }

}
