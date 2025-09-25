package com.hinterland.app.components.views
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.hinterland.app.R

class LoaderView(private val activity: Activity) {
    private var loaderView: View? = null

    // Show the loader
    fun show() {
        if (loaderView == null) {
            // Inflate loader view
            loaderView = LayoutInflater.from(activity).inflate(R.layout.loader_view, null)

            // Add loader view to the root layout
            activity.addContentView(
                loaderView,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
        loaderView?.visibility = View.VISIBLE
    }

    // Hide the loader
    fun hide() {
        loaderView?.visibility = View.GONE
    }
}
