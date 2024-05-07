package com.example.parking.ui.home.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.parking.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Using viewLifecycleOwner.lifecycleScope to launch a coroutine tied to the Fragment's lifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            // Repeat the following block while the Fragment's lifecycle is at least in STARTED state
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Call viewModel.getIncident(args.id) to fetch incident details
                viewModel.getIncident(args.id)

                // Collect the incident detail from the viewModel
                viewModel.incidentDetail.collect { incident ->
                    // Update UI with incident details
                    binding.title.text = incident.title
                    binding.description.text = incident.description
                }
            }
        }
    }
}
