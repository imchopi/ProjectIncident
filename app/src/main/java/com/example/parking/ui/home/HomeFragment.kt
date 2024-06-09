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
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
/**
 * A fragment representing the home screen of the app, displaying a list of incidents.
 */
class HomeFragment : Fragment() {

    // Declaring variables for Firebase authentication and Firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    // Binding for the fragment layout
    private lateinit var binding: FragmentHomeBinding

    // View model for handling data operations
    private val viewModel: HomeViewModel by viewModels()

    /**
     * Called to create the fragment's view hierarchy.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView() has returned.
     *
     * @param view The View returned by onCreateView().
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
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
                    val incidentsList = mutableListOf<IncidentsEntity>()
                    val incidentAdapter = mutableListOf<Incident>()
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

                        val incidentEntity = IncidentsEntity(
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
                        incidentsList.add(incidentEntity)
                        incidentAdapter.add(incident)
                    }
                    // Submit the list of incidents to the adapter
                    viewLifecycleOwner.lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            viewModel.uiState.collect {
                                adapter.submitList(incidentAdapter)
                            }
                        }
                    }
                    viewModel.insertAll(incidentsList)
                }.addOnFailureListener { exception ->
                    // Log any errors that occur during fetching
                    Log.e("Firestore", exception.toString())
                }
        }
    }

    /**
     * Function to navigate to the detail fragment when an incident is clicked.
     *
     * @param incident The incident that was clicked.
     */
    fun toDetail(incident: Incident) {
        // Create the navigation action to the detail fragment
        val action = incident.uuid?.let { HomeFragmentDirections.actionHomeToDetailFragment(it) }
        if (action != null) {
            // Navigate to the detail fragment
            findNavController().navigate(action)
        }
    }
}
