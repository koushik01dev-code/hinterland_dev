package com.hinterland.app.ui.findjobs

import android.util.Log
import com.hinterland.app.R
import com.hinterland.app.components.dialog.AlertType
import com.hinterland.app.components.network.model.request.SearchRequest
import com.hinterland.app.components.network.model.response.Filters
import com.hinterland.app.components.network.model.response.JobListing
import com.hinterland.app.components.network.model.response.OperatorProfile
import com.hinterland.app.components.network.model.response.OperatorsResponse
import com.hinterland.app.components.network.model.response.SearchOperatorsResponse
import com.hinterland.app.components.network.model.response.SortOption
import com.hinterland.app.ui.base.BasePresenter
import com.hinterland.app.utils.SharedStateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindJobsPresenter : BasePresenter<FindJobsView>() {
    private val interactor = FindJobsInteractor()
    private var allOperators: List<JobListing> = emptyList()
    var currentFilters: Filters = Filters()

    fun getAllOperators() {
        view?.showLoader()
        coroutineScope.launch {
            try {
                val response = interactor.getAllOperators()
                response.data?.operators?.let { operators ->
                    onGetAllOperatorsSuccess(operators)
                } ?: run {
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view?.showMessage("Error fetching operators: ${e.message}")
                }
            } finally {
                withContext(Dispatchers.Main) {
                    view?.hideLoader()
                }
            }
        }
    }

    private fun onGetAllOperatorsSuccess(operators: List<OperatorsResponse.Operator>) {
        allOperators = operators.map { operator ->
        JobListing(
            firstName = operator.user?.firstName.orEmpty(),
            lastName = operator.user?.lastName.orEmpty(),
            city = operator.user?.currentAddress?.city.orEmpty(),
            state = operator.user?.currentAddress?.line1.orEmpty(),
            rating = operator.rating?.toString().orEmpty(),
            totalReviews = operator.totalReviews?.toString().orEmpty(),
            profileImageUrl = operator.user?.profileImageUrl.orEmpty(),
            isAvailable = operator.isAvailable ?: false,
            id = operator.id ?: -1
        )
            }
        view?.renderJobs(allOperators)
    }

    fun searchOperator(skillId: Int) {
        view?.showLoader()
        coroutineScope.launch {
            try {
                val request = SearchRequest(
                    skillId = skillId,
                    sortByRating = true,
                    latitude = SharedStateUtils.latitude,
                    longitude = SharedStateUtils.longitude,
                    sortByExperience = true,
                    operatorLanguage = SharedStateUtils.selectedLanguage?: "English",
                    isVerified = true,
                    town = SharedStateUtils.town.toString(),
                    city = SharedStateUtils.city.toString(),
                    district = SharedStateUtils.district.toString(),
                    pinCode = SharedStateUtils.pinCode.toString(),
                    minRadius = 0,
                    maxRadius = 10,
                    operatorName = ""
                )

                val response = interactor.searchOperator(request)
                response?.let {
                    onSearchOperatorSuccess(it)
                } ?: run {
                    withContext(Dispatchers.Main) {
                        view?.showMessage("No data found")
                        view?.renderJobs(emptyList())
                    }
                }
            } catch (e: Exception) {
                Log.e("FindJobsPresenter", "Error calling searchOperator", e)
                view?.showMessage("Error loading jobs")
            } finally {
                view?.hideLoader()
            }
        }
    }

    private suspend fun onSearchOperatorSuccess(response: SearchOperatorsResponse) {
        if (response.status?.statusCode == 200) {
            val operators = response.data?.operators ?: emptyList()

            val jobListings = operators.map { operator ->
                JobListing(
                    id = operator.id ?: -1,
                    firstName = operator.user?.firstName.orEmpty(),
                    lastName = operator.user?.lastName.orEmpty(),
                    rating = operator.rating?.toString().orEmpty(),
                    totalReviews = operator.totalReviews?.toString().orEmpty(),
                    profileImageUrl = operator.user?.profileImageUrl.orEmpty(),
                    isAvailable = operator.isAvailable ?: false,
                    city = operator.user?.currentAddress?.city.orEmpty(),
                    state = operator.user?.currentAddress?.line1.orEmpty()
                )
            }

            withContext(Dispatchers.Main) {
                view?.renderJobs(jobListings)
            }
        } else {
            withContext(Dispatchers.Main) {
                view?.showCustomAlert(view?.getString(R.string.unexpected_error).toString(), view?.getString(R.string.default_alert_message), AlertType.ERROR)
            }
        }
    }

    fun onSortChanged(option: SortOption) {
        val sortedList = when (option) {
            SortOption.RATING_DESC -> allOperators.sortedByDescending { it.rating.toDoubleOrNull() ?: 0.0 }
        }
        view?.renderJobs(sortedList)
    }

    fun onFiltersChanged(filters: Filters) {
        currentFilters = filters
        var filteredList = allOperators

        // Verified filter
        if (filters.verifiedUsersOnly) {
            filteredList = filteredList.filter { it.isVerified } // make sure JobListing has isVerified field
        }

        // Available filter
        if (filters.availableOnly) {
            filteredList = filteredList.filter { it.isAvailable }
        }

        // Language filter
        filters.language?.let { lang ->
            filteredList = filteredList.filter { job ->
                job.language.equals(lang.displayName, ignoreCase = true)
            }
        }

        // Distance filter
        filters.maxDistance?.let { maxDist ->
            filteredList = filteredList.filter { job ->
                (job.distance ?: Int.MAX_VALUE) <= maxDist
            }
        }

        view?.renderJobs(filteredList)
    }

}


