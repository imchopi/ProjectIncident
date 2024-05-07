package com.example.parking.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.parking.data.db.incidents.Incident
import com.example.parking.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    // Declaring variables for Firebase authentication and Firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    // Binding for the fragment layout
    private lateinit var binding: FragmentHomeBinding

    // View model for handling data operations
    private val viewModel: HomeViewModel by viewModels()

    // Called to create the fragment's view hierarchy
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Called immediately after onCreateView() has returned
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter for the RecyclerView
        val adapter = HomeAdapter(requireContext(), ::toDetail)
        val recyclerView = binding.incidents
        recyclerView.adapter = adapter

        // Retrieve current user's ID from Firebase authentication
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        // Fetch incidents data for the current user from Firestore
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
                    // Submit the list of incidents to the adapter
                    adapter.submitList(incidentsList)
                }.addOnFailureListener { exception ->
                    // Log any errors that occur during fetching
                    Log.e("Firestore", "Error fetching documents: $exception")
                }
        }
    }

    // Function to navigate to detail fragment when an incident is clicked
    fun toDetail(incident: Incident) {
        // Create the navigation action to the detail fragment
        val action = incident.uuid?.let { HomeFragmentDirections.actionHomeToDetailFragment(it) }
        if (action != null) {
            // Navigate to the detail fragment
            findNavController().navigate(action)
        }
    }
}
