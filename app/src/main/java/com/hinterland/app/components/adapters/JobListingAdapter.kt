package com.hinterland.app.components.adapters

import android.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hinterland.app.components.network.model.response.JobListing
import com.hinterland.app.databinding.ItemJobCardBinding

class JobListingAdapter : ListAdapter<JobListing, JobListingAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<JobListing>() {
        override fun areItemsTheSame(oldItem: JobListing, newItem: JobListing): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: JobListing, newItem: JobListing): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemJobCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(private val binding: ItemJobCardBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "DefaultLocale")
        fun bind(item: JobListing) {
            Glide.with(binding.imageAvatar.context)
                .load(item.profileImageUrl)
                .placeholder(R.drawable.ic_delete)
                .into(binding.imageAvatar)
            binding.firstName.text = item.firstName
            binding.lastName.text = item.lastName
            val ratingValue = item.rating.toDoubleOrNull() ?: 0.0
            binding.textRating.text = String.format("%.1f", ratingValue)
            binding.textReviews.text = item.totalReviews
            binding.location.text = listOf(item.city, item.state).filter { it.isNotBlank() }.joinToString(", ")
        }
    }
}