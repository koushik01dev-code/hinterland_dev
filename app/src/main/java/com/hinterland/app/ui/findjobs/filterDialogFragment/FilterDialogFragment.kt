package com.hinterland.app.ui.findjobs.filterDialogFragment

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.graphics.drawable.toDrawable
import com.google.gson.Gson
import com.hinterland.app.components.dialog.AlertType
import com.hinterland.app.components.network.model.response.Filters
import com.hinterland.app.components.network.model.response.Language
import com.hinterland.app.databinding.DialogFiltersBinding
import com.hinterland.app.ui.base.BaseDialogFragment

class FilterDialogFragment : BaseDialogFragment<FilterDialogFragmentPresenter, FilterDialogFragmentView, DialogFiltersBinding>(),
    FilterDialogFragmentView {

    var onApply: ((Filters) -> Unit)? = null
    private val gson = Gson()
    private var currentFilters: Filters = Filters()

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): DialogFiltersBinding {
        return DialogFiltersBinding.inflate(inflater, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    override fun providePresenter(): FilterDialogFragmentPresenter {
        return FilterDialogFragmentPresenter()
    }

    override fun provideView(): FilterDialogFragmentView = this

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val json = requireArguments().getString("filters")
        currentFilters = if (!json.isNullOrEmpty()) {
            gson.fromJson(json, Filters::class.java)
        } else {
            Filters()
        }

        val languages = Language.entries
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, languages)
        binding.spinnerLanguage.setAdapter(adapter)

        currentFilters.language?.let {
            binding.spinnerLanguage.setText(it.displayName, false)
        }
        binding.sliderDistance.value = (currentFilters.maxDistance ?: 25).toFloat()

        // Handle Apply button
        binding.buttonApply.setOnClickListener {
            val selectedLanguageText = binding.spinnerLanguage.text.toString()
            val selectedLanguage = Language.entries.find { it.displayName == selectedLanguageText }

            val newFilters = Filters(
                verifiedUsersOnly = binding.switchVerified.isChecked,
                availableOnly = binding.switchAvailable.isChecked,
                language = selectedLanguage,
                maxDistance = binding.sliderDistance.value.toInt()
            )

            onApply?.invoke(newFilters)
            dismiss()
        }
    }

    override fun showMessage(message: String) {
    }

    override fun showCustomAlert(message: String, title: String?, alertType: AlertType, callback: (() -> Unit)?) {}

    override fun showLoader() {
    }

    override fun hideLoader() {
    }

    companion object {
        fun newInstance(filters: Filters): FilterDialogFragment {
            val f = FilterDialogFragment()
            f.arguments = Bundle().apply {
                putString("filters", Gson().toJson(filters))
            }
            return f
        }
    }
}