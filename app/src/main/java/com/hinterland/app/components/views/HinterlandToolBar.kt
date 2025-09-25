package com.hinterland.app.components.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hinterland.app.R
import androidx.core.content.withStyledAttributes

class HinterlandToolBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    private val titleTextView: TextView
    private val backButton: View
    private val shareButton: ImageView
    private val secondButton: ImageView

    init {
        // Inflate the layout
        LayoutInflater.from(context).inflate(R.layout.view_hinterland_toolbar, this, true)

        // Initialize views
        titleTextView = findViewById(R.id.title)
        backButton = findViewById(R.id.back_button)
        shareButton = findViewById(R.id.share_button)
        secondButton = findViewById(R.id.second_button)

        // Process custom attributes
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.HinterlandToolBar, 0, 0) {
                val titleText = getString(R.styleable.HinterlandToolBar_title)
                titleTextView.text = titleText
            }
        }

        fun setSecondButtonSize(width: Int, height: Int) {
            val layoutParams = secondButton.layoutParams
            layoutParams.width = dpToPx(width)
            layoutParams.height = dpToPx(height)
            secondButton.layoutParams = layoutParams
        }

        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(v.paddingLeft, sysBars.top, v.paddingRight, v.paddingBottom)
            insets
        }
    }

    fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    // Method to set the title text programmatically
    fun setTitleText(title: String) {
        titleTextView.text = title
    }

    // Method to set the back button click listener
    fun setOnBackButtonClickListener(listener: OnClickListener) {
        backButton.setOnClickListener(listener)
    }

    // Method to show/hide the back button
    fun showBackButton(show: Boolean) {
        backButton.visibility = if (show) VISIBLE else GONE
    }

    // Method to set the second button click listener
    fun setOnSecondButtonClickListener(listener: OnClickListener) {
        secondButton.setOnClickListener(listener)
    }

    // Method to show/hide the second button
    private fun showSecondButton(show: Boolean) {
        secondButton.visibility = if (show) VISIBLE else GONE
    }

    // Method to dynamically set the drawable for the second button
    fun setSecondButtonImage(drawableResId: Int?) {
        if (drawableResId != null) {
            secondButton.setImageDrawable(ContextCompat.getDrawable(context, drawableResId))
            showSecondButton(true)
        } else {
            showSecondButton(false)
        }
    }
}
