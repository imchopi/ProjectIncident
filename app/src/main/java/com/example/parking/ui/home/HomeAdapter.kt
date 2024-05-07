package com.example.parking.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parking.data.db.incidents.Incident
import com.example.parking.databinding.IncidentItemBinding

class HomeAdapter(
    private val context: Context,
    val onClick:((Incident) -> Unit)
) : ListAdapter<Incident, HomeAdapter.IncidentViewHolder>(IncidentDiffCallback) {

    // ViewHolder class for each incident item
    inner class IncidentViewHolder(private val binding: IncidentItemBinding): RecyclerView.ViewHolder(binding.root) {
        // Binds an incident to the ViewHolder
        fun bind (incident: Incident) {
            binding.incidentTitle.text = incident.title
            // Set click listener to handle item clicks
            binding.root.setOnClickListener {
                onClick(incident)
            }
        }
    }

    // Create ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeAdapter.IncidentViewHolder = IncidentViewHolder(IncidentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    // Bind ViewHolder with data
    override fun onBindViewHolder(holder: HomeAdapter.IncidentViewHolder, position: Int) = holder.bind(getItem(position))

    // DiffUtil callback to efficiently update the RecyclerView items
    private object IncidentDiffCallback: DiffUtil.ItemCallback<Incident>() {
        override fun areItemsTheSame(oldItem: Incident, newItem: Incident) = oldItem.uuid == newItem.uuid
        override fun areContentsTheSame(oldItem: Incident, newItem: Incident) = oldItem == newItem
    }
}
