package com.hinterland.app.ui.landingpage

import com.hinterland.app.components.network.model.response.OperatorsResponse
import com.hinterland.app.components.network.model.response.SkillListResponse
import com.hinterland.app.ui.base.BaseView

interface LandingPageView : BaseView {
    fun setSkills(response: SkillListResponse)
    fun startAutoSlide(dummyOperators: OperatorsResponse)
}