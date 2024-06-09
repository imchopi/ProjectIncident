package com.example.parking.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parking.data.db.incidents.Incident
import com.example.parking.databinding.IncidentItemBinding

/**
 * Adapter class for displaying a list of incidents in a RecyclerView.
 *
 * @property context The context in which the adapter is used.
 * @property onClick A lambda function to handle item clicks.
 */
class HomeAdapter(
    private val context: Context,
    val onClick: ((Incident) -> Unit)
) : ListAdapter<Incident, HomeAdapter.IncidentViewHolder>(IncidentDiffCallback) {

    /**
     * ViewHolder class for each incident item.
     *
     * @property binding The binding object for the incident item layout.
     */
    inner class IncidentViewHolder(private val binding: IncidentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds an incident to the ViewHolder.
         *
         * @param incident The incident data to bind to the ViewHolder.
         */
        fun bind(incident: Incident) {
            binding.incidentTitle.text = incident.title
            // Set click listener to handle item clicks
            binding.root.setOnClickListener {
                onClick(incident)
            }
        }
    }

    /**
     * Creates a new ViewHolder.
     *
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new IncidentViewHolder instance.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeAdapter.IncidentViewHolder = IncidentViewHolder(IncidentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    /**
     * Binds the ViewHolder with data.
     *
     * @param holder The ViewHolder to be bound.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: HomeAdapter.IncidentViewHolder, position: Int) = holder.bind(getItem(position))

    /**
     * DiffUtil callback to efficiently update the RecyclerView items.
     */
    private object IncidentDiffCallback : DiffUtil.ItemCallback<Incident>() {

        /**
         * Checks if two items have the same ID.
         *
         * @param oldItem The old item to be compared.
         * @param newItem The new item to be compared.
         * @return True if the items have the same ID, false otherwise.
         */
        override fun areItemsTheSame(oldItem: Incident, newItem: Incident) = oldItem.uuid == newItem.uuid

        /**
         * Checks if the contents of two items are the same.
         *
         * @param oldItem The old item to be compared.
         * @param newItem The new item to be compared.
         * @return True if the contents of the items are the same, false otherwise.
         */
        override fun areContentsTheSame(oldItem: Incident, newItem: Incident) = oldItem == newItem
    }
}
