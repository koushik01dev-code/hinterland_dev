package com.hinterland.app.ui.landingpage

import android.util.Log
import com.hinterland.app.components.network.model.response.OperatorsResponse
import com.hinterland.app.components.network.model.response.SkillListResponse
import com.hinterland.app.ui.base.BasePresenter
import com.hinterland.app.ui.findjobs.FindJobsInteractor
import com.hinterland.app.utils.SharedStateUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LandingPagePresenter : BasePresenter<LandingPageView>() {
    private val landingPageInteractor = LandingPageInteractor()
    private val findJobInteractor = FindJobsInteractor()

    //method to call getAllSkills
    fun getAllSkillsAndNearByOperators() {
        view?.showLoader()
        coroutineScope.launch {
            try {
                val skillsDeferred = async { landingPageInteractor.getAllSkills() }
                val operatorsDeferred = async { findJobInteractor.getAllOperators() }

                val skillsResponse = skillsDeferred.await()
                val operatorsResponse = operatorsDeferred.await()

                // Only call when both are not null
                if (skillsResponse != null && operatorsResponse != null) {
                    onGetSkillsAndOperatorsSuccess(skillsResponse, operatorsResponse)
                } else {
                    Log.e("LandingPagePresenter", "One of the responses is null")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("LandingPagePresenter", "Error fetching data", e)
            } finally {
                view?.hideLoader()
            }
        }
    }

    fun onGetSkillsAndOperatorsSuccess(response: SkillListResponse, operatorsResponse: OperatorsResponse) {
        Log.d("skills ", "skills $response")
        val skillNames = response.data?.mapNotNull { it.name } ?: emptyList()
        SharedStateUtils.allSkills = response.data ?: emptyList()
        view?.setSkills(response)
        view?.startAutoSlide(operatorsResponse)
    }

}

