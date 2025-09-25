package com.hinterland.app.components.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.hinterland.app.R

enum class AlertType {
    SUCCESS,
    ERROR,
    WARNING,
    CONFIRM
}

class AlertDialog : DialogFragment() {
    private var alertMessage: String? = null
    private var alertMessageResId: Int? = null
    private var alertTitle: String = "Alert"
    private var alertTitleResId: Int? = null
    private var alertType: AlertType = AlertType.ERROR
    private var callback: (() -> Unit)? = null

    init {
        isCancelable = false
    }

    fun setMessage(message: String) {
        alertMessage = message
    }

    fun setMessageRes(resId: Int) {
        alertMessageResId = resId
    }

    fun setTitle(title: String?) {
        alertTitle = title ?: "Alert"
    }

    fun setTitleRes(resId: Int) {
        alertTitleResId = resId
    }

    fun setAlertType(type: AlertType) {
        alertType = type
    }

    fun setOkButtonCallback(callback: () -> Unit) {
        this.callback = callback
    }

    private fun determineIconResId(): Int {
        return when (alertType) {
            AlertType.SUCCESS -> R.drawable.img
            AlertType.ERROR -> R.drawable.img
            AlertType.WARNING -> android.R.drawable.ic_dialog_alert
            AlertType.CONFIRM -> android.R.drawable.ic_dialog_info
        }
    }

    private fun determineTitleColorResId(): Int {
        return when (alertType) {
            AlertType.SUCCESS -> R.color.green
            AlertType.ERROR -> R.color.bd_red
            AlertType.WARNING -> R.color.warningColor
            AlertType.CONFIRM -> R.color.purplr_r
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_alert, container, false)

        val iconView: ImageView = view.findViewById(R.id.alertIcon)
        val titleView: TextView = view.findViewById(R.id.alertTitle)
        val messageView: TextView = view.findViewById(R.id.alertMessage)
        val okButton: Button = view.findViewById(R.id.alertOkButton)

        iconView.setImageResource(determineIconResId())
        titleView.text = alertTitleResId?.let { getString(it) } ?: alertTitle
        messageView.text = alertMessageResId?.let { getString(it) } ?: alertMessage
                ?: getString(R.string.default_alert_message)

        titleView.setTextColor(ContextCompat.getColor(requireContext(), determineTitleColorResId()))

        okButton.setOnClickListener {
            callback?.invoke()
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val margin = 50
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels - 2 * margin),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}


