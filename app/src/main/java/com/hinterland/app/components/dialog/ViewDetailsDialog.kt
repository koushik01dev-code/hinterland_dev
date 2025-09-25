package com.hinterland.app.components.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.hinterland.app.R

class ViewDetailsDialog : DialogFragment() {

    companion object {
        private const val ARG_FIRST_NAME = "firstName"
        private const val ARG_LAST_NAME = "lastName"
        private const val ARG_LOCATION = "location"
        private const val ARG_BIO = "bio"
        private const val ARG_EXPERIENCE = "experience"
        private const val ARG_GENDER = "gender"

        fun newInstance(
            firstName: String,
            lastName: String,
            location: String,
            bio: String,
            experience: String,
            gender: String
        ): ViewDetailsDialog {
            val dialog = ViewDetailsDialog()
            val args = Bundle().apply {
                putString(ARG_FIRST_NAME, firstName)
                putString(ARG_LAST_NAME, lastName)
                putString(ARG_LOCATION, location)
                putString(ARG_BIO, bio)
                putString(ARG_EXPERIENCE, experience)
                putString(ARG_GENDER, gender)
            }
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_view_details, container, false)

        val firstName = arguments?.getString(ARG_FIRST_NAME)
        val lastName = arguments?.getString(ARG_LAST_NAME)
        val location = arguments?.getString(ARG_LOCATION)
        val bio = arguments?.getString(ARG_BIO)
        val experience = arguments?.getString(ARG_EXPERIENCE)
        val gender = arguments?.getString(ARG_GENDER)

        // Bind UI
        view.findViewById<TextView>(R.id.tvJobTitle).text = "$firstName $lastName"
        view.findViewById<TextView>(R.id.tvDescription).text = bio
        view.findViewById<TextView>(R.id.tvBudget).text = "Experience: $experience"
        view.findViewById<TextView>(R.id.tvHours).text = "Gender: $gender"
        view.findViewById<TextView>(R.id.tvDate).text = "Location: $location"

        view.findViewById<Button>(R.id.btnClose).setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val margin = 50
        // Make dialog width match parent
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels - 2 * margin),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}