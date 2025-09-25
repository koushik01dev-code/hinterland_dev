package com.hinterland.app.ui.findjobs

import com.hinterland.app.components.network.model.response.JobListing
import com.hinterland.app.components.network.model.response.OperatorProfile
import com.hinterland.app.ui.base.BaseView

interface FindJobsView : BaseView {
    fun renderJobs(allOperators: List<JobListing>)
    fun getString(resId: Int): String
}