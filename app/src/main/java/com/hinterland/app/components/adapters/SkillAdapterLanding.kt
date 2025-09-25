package com.hinterland.app.components.adapters

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hinterland.app.components.network.model.response.SkillListResponse
import com.hinterland.app.databinding.ItemExploreMoreBinding
import com.hinterland.app.databinding.ItemSkillBinding

class SkillAdapterLanding(
    private val skills: List<SkillListResponse.Skill>,
    private val onExploreClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SKILL = 0
        private const val TYPE_EXPLORE = 1
    }

    inner class SkillViewHolder(val binding: ItemSkillBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(skill: SkillListResponse.Skill) {
            binding.apply {
                tvSkillName.text = skill.name
                tvSkillDescription.text = skill.description

                Glide.with(ivSkillImage.context)
                    .load(skill.skillImageUrl)
                    .placeholder(R.drawable.ic_delete)
                    .into(ivSkillImage)
            }
        }
    }

    inner class ExploreViewHolder(val binding: ItemExploreMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
                onExploreClick()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val maxSkills = 6
        return if (position < minOf(skills.size, maxSkills)) TYPE_SKILL else TYPE_EXPLORE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SKILL) {
            val binding = ItemSkillBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            SkillViewHolder(binding)
        } else {
            val binding = ItemExploreMoreBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ExploreViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SkillViewHolder) {
            holder.bind(skills[position])
        } else if (holder is ExploreViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        val maxSkills = 6
        return if (skills.size > maxSkills) maxSkills + 1 else skills.size
    }
}
