package com.hinterland.app.ui.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.MenuInflater
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hinterland.app.R
import com.hinterland.app.ui.base.BaseActivity
import com.hinterland.app.components.lang.LocaleManager
import com.hinterland.app.databinding.ActivityLoginBinding
import com.hinterland.app.ui.findjobs.FindJobsActivity
import com.hinterland.app.ui.landingpage.LandingPageActivity
import com.hinterland.app.ui.register.RegisterActivity
import com.hinterland.app.utils.SharedStateUtils
import java.util.Locale

class LoginActivity : BaseActivity<LoginPresenter, LoginView, ActivityLoginBinding>(), LoginView {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionReqCode = 1001

    override fun provideViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun providePresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun provideView(): LoginView {
        return this
    }

    override fun showMessage(message: String) {
        val isSuccess = message.contains("success", ignoreCase = true)
        if (isSuccess) {
            println("Login Success")
        } else {
            binding.tvLoginError.text = message
            binding.tvLoginError.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding is ready here
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        setSupportActionBar(binding.toolbar)
        // Push toolbar content below status bar in edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(
                v.paddingLeft,
                sysBars.top,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }

        binding.btnLogin.isEnabled = false
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            presenter.onLoginButtonClicked(email, password, this)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Enable login only when both fields non-empty
        val watcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val u = binding.etEmail.text?.toString().orEmpty().isNotBlank()
                val p = binding.etPassword.text?.toString().orEmpty().isNotBlank()
                binding.btnLogin.isEnabled = u && p
            }
        }
        binding.etEmail.addTextChangedListener(watcher)
        binding.etPassword.addTextChangedListener(watcher)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_login, menu)
        val item = menu?.findItem(R.id.action_language)
        if (item != null) {
            val anchor = ImageView(this)
            anchor.setImageResource(R.drawable.ic_more_vert_24)
            anchor.contentDescription = getString(R.string.language_choose_title)
            anchor.setOnClickListener { view ->
                showLanguageOverflow(view)
            }
            item.actionView = anchor
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun showLanguageChooser() {
        val labels = arrayOf(
            getString(R.string.language_english),
            getString(R.string.language_hindi),
            getString(R.string.language_kannada)
        )
        val codes = arrayOf("en", "hi", "kn")
        val current = LocaleManager(this).getLanguage()
        var selectedIndex = codes.indexOf(current).let { if (it >= 0) it else 0 }

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.language_choose_title))
            .setSingleChoiceItems(labels, selectedIndex) { _, which ->
                selectedIndex = which
            }
            .setPositiveButton(getString(R.string.action_done)) { dialog, _ ->
                val code = codes[selectedIndex]
                LocaleManager(this).setLanguage(code)
                SharedStateUtils.selectedLanguage = code
                recreate()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.action_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    @SuppressLint("RestrictedApi")
    private fun showLanguageOverflow(anchor: View) {
        val menuBuilder = MenuBuilder(this)
        MenuInflater(this).inflate(R.menu.menu_login, menuBuilder)
        menuBuilder.setCallback(object : MenuBuilder.Callback {
            override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_language -> {
                        showLanguageChooser()
                        true
                    }
                    else -> false
                }
            }

            override fun onMenuModeChange(menu: MenuBuilder) {}
        })

        val helper = MenuPopupHelper(this, menuBuilder, anchor)
        helper.setForceShowIcon(false)
        helper.gravity = Gravity.END
        helper.show(0, anchor.height + dpToPx(8))
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionReqCode
            )
        } else {
            if (isLocationEnabled()) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Please enable location services", Toast.LENGTH_LONG).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateAgeMillis(0)
            .build()

        fusedLocationClient.getCurrentLocation(request, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    SharedStateUtils.latitude = latitude
                    SharedStateUtils.longitude = longitude

                    fetchAddress(latitude, longitude)
                } else {
                    Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fetchAddress(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    if (addresses.isNotEmpty()) {
                        saveAddressToPrefs(addresses[0])
                    }
                }
            })
        } else {
            Thread {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    saveAddressToPrefs(addresses[0])
                }
            }.start()
        }
    }

    private fun saveAddressToPrefs(address: Address) {
        val town = address.subLocality ?: ""
        val city = address.locality ?: ""
        val district = address.subAdminArea ?: ""
        val pinCode = address.postalCode ?: ""

        SharedStateUtils.town = town
        SharedStateUtils.city = city
        SharedStateUtils.district = district
        SharedStateUtils.pinCode = pinCode

        runOnUiThread {
            binding.tvLoginError.text = "Location saved: $town, $city, $district, $pinCode"
            binding.tvLoginError.visibility = View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionReqCode &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    //method to redirect landing page
    override fun landingScreen() {
        val intent = Intent(this, LandingPageActivity::class.java)
        startActivity(intent)
    }

}


