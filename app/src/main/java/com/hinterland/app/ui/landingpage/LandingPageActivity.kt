package com.hinterland.app.ui.landingpage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hinterland.app.R
import com.hinterland.app.components.adapters.LandingPageAdapter
import com.hinterland.app.components.adapters.SkillAdapterLanding
import com.hinterland.app.components.dialog.ViewDetailsDialog
import com.hinterland.app.components.network.model.response.Operator
import com.hinterland.app.components.network.model.response.OperatorsResponse
import com.hinterland.app.components.network.model.response.SkillListResponse
import com.hinterland.app.databinding.ActivityLandingPageBinding
import com.hinterland.app.ui.base.BaseActivity
import com.hinterland.app.ui.findjobs.FindJobsActivity
import com.hinterland.app.ui.profile.ProfileActivity
import com.hinterland.app.utils.SharedStateUtils


class LandingPageActivity :
    BaseActivity<LandingPagePresenter, LandingPageView, ActivityLandingPageBinding>(),
    LandingPageView {

    private lateinit var adapter: LandingPageAdapter
    private val sliderHandler = Handler(Looper.getMainLooper())
    private lateinit var layoutManager: LinearLayoutManager

    override fun provideViewBinding(): ActivityLandingPageBinding {
        return ActivityLandingPageBinding.inflate(layoutInflater)
    }

    override fun providePresenter(): LandingPagePresenter {
        return LandingPagePresenter()
    }

    override fun provideView(): LandingPageView {
        return this
    }

    override fun showMessage(message: String) {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBar()
        presenter.getAllSkillsAndNearByOperators()
        binding.btnSearchOperators.setOnClickListener {
            val intent = Intent(this, FindJobsActivity::class.java)
            startActivity(intent)
        }
        binding.scrollView.visibility = View.GONE
        profilePageClick()

    }

    //method to set Toolbar
    private fun setToolBar() {
        binding.toolbar.setSecondButtonImage(R.drawable.profile_icon_blue_2x)
        binding.toolbar.setOnBackButtonClickListener() {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                } else {
                    finish()
                }
            }
        })

        binding.toolbar.setOnSecondButtonClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
            binding.name.text = SharedStateUtils.username
            binding.place.text = SharedStateUtils.city
        }
    }

    override fun startAutoSlide(slidevalues: OperatorsResponse) {
        val operatorsList = mapOperators(slidevalues)
        setupRecyclerView(operatorsList)
        setupDotsIndicator(operatorsList.size)
        startAutoScroll(operatorsList.size)
    }

    //method to set skills
    override fun setSkills(response: SkillListResponse) {
        val adapter = SkillAdapterLanding(response.data) {
            val intent = Intent(this, FindJobsActivity::class.java)
            startActivity(intent)
        }
        binding.rvSkills.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvSkills.adapter = adapter
        binding.scrollView.visibility = View.VISIBLE
    }

    //method to crete dots
    private fun createDots(count: Int, activeIndex: Int) {
        binding.dotsContainer.removeAllViews()
        for (i in 0 until count) {
            val dot = View(this)
            val size = 16
            val params = LinearLayout.LayoutParams(size, size)
            params.marginEnd = 8
            dot.layoutParams = params
            dot.setBackgroundResource(
                if (i == activeIndex) R.drawable.active_dot else R.drawable.inactive_dot
            )
            binding.dotsContainer.addView(dot)
        }
    }

    //method to map operators
    private fun mapOperators(slidevalues: OperatorsResponse): List<Operator> {
        val apiOperators = slidevalues.data?.operators ?: emptyList()
        return apiOperators.map { operator ->
            Operator(
                firstName = operator.user?.firstName.orEmpty(),
                lastName = operator.user?.lastName.orEmpty(),
                gender = operator.user?.gender.orEmpty(),
                location = listOfNotNull(
                    operator.user?.currentAddress?.line1,
                    operator.user?.currentAddress?.state,
                    operator.user?.currentAddress?.country
                ).joinToString(", "),
                bio = operator.bio.orEmpty(),
                experience = operator.operatorSkills
                    ?.firstOrNull()
                    ?.experienceYears
                    ?.let { "$it yrs" } ?: "N/A"
            )
        }
    }

    //method to set recyclerview
    private fun setupRecyclerView(operators: List<Operator>) {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = LandingPageAdapter(operators,this)
        binding.rvOperators.apply {
            layoutManager = this@LandingPageActivity.layoutManager
            adapter = this@LandingPageActivity.adapter
        }
    }

    //method to set dot indicators
    private fun setupDotsIndicator(itemCount: Int) {
        binding.rvOperators.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val current = layoutManager.findFirstVisibleItemPosition()
                createDots(itemCount, current)
            }
        })
    }

    //method for operator card scroll
    private fun startAutoScroll(itemCount: Int) {
        var scrollDirection = 1
        val runnable = object : Runnable {
            override fun run() {
                val current = layoutManager.findFirstVisibleItemPosition()
                var next = current + scrollDirection

                if (next !in 0 until itemCount) {
                    scrollDirection *= -1
                    next = current + scrollDirection
                }

                binding.rvOperators.smoothScrollToPosition(next)
                sliderHandler.postDelayed(this, 5000)
            }
        }
        sliderHandler.post(runnable)
    }

    //redirect to profile screen
    private fun profilePageClick() {
        binding.updateProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
    }

    fun setViewData(operator: Operator) {
        val dialog = ViewDetailsDialog.newInstance(
            operator.firstName,
            operator.lastName,
            operator.location,
            operator.bio,
            operator.experience,
            operator.gender
        )
        dialog.show(supportFragmentManager, "ViewDetailsDialog")
    }
}