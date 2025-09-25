package com.hinterland.app.ui.findjobs

import android.R
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.hinterland.app.components.adapters.JobListingAdapter
import com.hinterland.app.components.network.model.response.JobListing
import com.hinterland.app.components.network.model.response.SortOption
import com.hinterland.app.databinding.ActivityFindJobsBinding
import com.hinterland.app.ui.base.BaseActivity
import com.hinterland.app.ui.findjobs.filterDialogFragment.FilterDialogFragment
import com.hinterland.app.utils.SharedStateUtils

class FindJobsActivity : BaseActivity<FindJobsPresenter, FindJobsView, ActivityFindJobsBinding>(),
    FindJobsView {

    private lateinit var jobsAdapter: JobListingAdapter
    private val skillNames: List<String> get() = SharedStateUtils.allSkills.map { it.name }
    private var selectedSkillId: Int? = null

    override fun provideViewBinding(): ActivityFindJobsBinding {
        return ActivityFindJobsBinding.inflate(layoutInflater)
    }

    override fun providePresenter(): FindJobsPresenter {
        return FindJobsPresenter()
    }

    override fun provideView(): FindJobsView = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecycler()
        setupSearchDropdown()
        setupSearch()
        setupBottomBar()
        presenter.getAllOperators()
        binding.toolbar.setOnBackButtonClickListener {
            finish()
        }
    }

    private fun setupRecycler() {
        jobsAdapter = JobListingAdapter()
        binding.recyclerJobs.apply {
            layoutManager = LinearLayoutManager(this@FindJobsActivity)
            adapter = jobsAdapter
        }
    }

    private fun setupSearchDropdown() {
        // Attach adapter
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_dropdown_item_1line,
            skillNames
        )
        binding.inputSearch.setAdapter(adapter)

        // Track selected skill
        binding.inputSearch.setOnItemClickListener { _, _, position, _ ->
            val selectedName = adapter.getItem(position)
            val selectedSkill = SharedStateUtils.allSkills.find { it.name == selectedName }
            selectedSkillId = selectedSkill?.id
        }
    }

    private fun setupSearch() {
        binding.inputSearch.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)) {
                selectedSkillId?.let {
                    presenter.searchOperator(it)
                }
                true
            } else {
                false
            }
        }
    }

    private fun setupBottomBar() {
        binding.buttonSort.setOnClickListener { showSortDialog() }
        binding.buttonFilter.setOnClickListener { showFilterDialog() }
    }

    private fun showSortDialog() {
        val options = arrayOf(
            "Highest rating",
            "Experience"
        )
        MaterialAlertDialogBuilder(this)
            .setTitle("Sort by")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> presenter.onSortChanged(SortOption.RATING_DESC)
                }
            }
            .show()
    }

    private fun showFilterDialog() {
        val dialog = FilterDialogFragment.newInstance(presenter.currentFilters)
        dialog.onApply = { filters -> presenter.onFiltersChanged(filters) }
        dialog.show(supportFragmentManager, "filters")
    }

    override fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun renderJobs(allOperators: List<JobListing>) {
        jobsAdapter.submitList(allOperators)
        binding.emptyView.root.visibility = if (allOperators.isEmpty()) View.VISIBLE else View.GONE
    }
}


