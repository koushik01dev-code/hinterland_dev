package com.hinterland.app.components.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hinterland.app.components.network.model.response.Operator
import com.hinterland.app.databinding.ItemOperatorBinding
import com.hinterland.app.ui.landingpage.LandingPageActivity

class LandingPageAdapter(private val operators: List<Operator>, private val listener: LandingPageActivity) :
    RecyclerView.Adapter<LandingPageAdapter.OperatorViewHolder>() {

    inner class OperatorViewHolder(val binding: ItemOperatorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(operator: Operator) {
            binding.tvName.text = "${operator.firstName} ${operator.lastName}"
            binding.tvLocation.text = operator.location
            binding.bio.text = operator.bio
            binding.tvExperience.text = "Experience: ${operator.experience}"
            binding.tvGender.text = operator.gender

            // Optional: set click listeners
            binding.btnView.setOnClickListener {
                listener.setViewData(operator)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperatorViewHolder {
        val binding = ItemOperatorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OperatorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperatorViewHolder, position: Int) {
        holder.bind(operators[position])
    }

    override fun getItemCount() = operators.size
}