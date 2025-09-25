package com.hinterland.app.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import com.hinterland.app.R
import com.hinterland.app.components.dialog.AlertDialog
import com.hinterland.app.components.dialog.AlertType
import com.hinterland.app.databinding.ActivityRegisterBinding
import com.hinterland.app.ui.base.BaseActivity
import com.hinterland.app.ui.login.LoginActivity

class RegisterActivity : BaseActivity<RegisterPresenter, RegisterView, ActivityRegisterBinding>(),
    RegisterView {

    override fun provideViewBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun providePresenter(): RegisterPresenter {
        return RegisterPresenter()
    }

    override fun provideView(): RegisterView {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val genderAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listOf("Male", "Female", "Other")
        )
        binding.spGender.setAdapter(genderAdapter)
        binding.btnRegister.isEnabled = false
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val phone = binding.etPhone.text.toString()
            val gender = binding.spGender.text?.toString().orEmpty()
            presenter.onRegisterClicked(username, email, password, confirmPassword, firstName,
                lastName, phone, gender, this)
        }

        binding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString().orEmpty()
                if (text.length != 10) {
                    binding.etPhone.error = "Enter exactly 10 digits"
                } else {
                    binding.etPhone.error = null
                }
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString().orEmpty()
                if (text.length < 8) {
                    binding.etPassword.error = "Minimum 8 characters"
                } else {
                    binding.etPassword.error = null
                }
                // Re-validate confirm password on password change
                val confirm = binding.etConfirmPassword.text?.toString().orEmpty()
                if (confirm.isNotEmpty() && confirm != text) {
                    binding.etConfirmPassword.error = "Passwords do not match"
                } else {
                    binding.etConfirmPassword.error = null
                }
                updateRegisterButtonState()
            }
        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val email = s?.toString().orEmpty()
                if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etEmail.error = "Please enter a valid email address"
                } else {
                    binding.etEmail.error = null
                }
                updateRegisterButtonState()
            }
        })

        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val confirm = s?.toString().orEmpty()
                val password = binding.etPassword.text?.toString().orEmpty()
                if (confirm.isNotEmpty() && confirm != password) {
                    binding.etConfirmPassword.error = "Passwords do not match"
                } else {
                    binding.etConfirmPassword.error = null
                }
                updateRegisterButtonState()
            }
        })

        binding.etUsername.addTextChangedListener(simpleWatcher)
        binding.etFirstName.addTextChangedListener(simpleWatcher)
        binding.etLastName.addTextChangedListener(simpleWatcher)

        binding.spGender.setOnItemClickListener { _, _, _, _ ->
            updateRegisterButtonState()
        }

        updateRegisterButtonState()
        setupToolbar()
        binding.btnBackToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onRegisterSuccess() {
        Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
    }

    override fun onRegisterError(message: String) {
        val dialog = AlertDialog().apply {
            setTitle("Error")
            setMessage(message.ifBlank { getString(R.string.default_alert_message) })
            setAlertType(AlertType.ERROR)
        }
        dialog.show(supportFragmentManager, "register_error_dialog")
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupToolbar() {
        binding.toolbar.setTitleText(getString(R.string.register_button))
        binding.toolbar.setOnBackButtonClickListener { finish() }
    }

    private val simpleWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            updateRegisterButtonState()
        }
    }

    private fun updateRegisterButtonState() {
        val usernameOk = binding.etUsername.text?.toString().orEmpty().isNotBlank()
        val emailText = binding.etEmail.text?.toString().orEmpty()
        val emailOk = emailText.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(emailText).matches()
        val passText = binding.etPassword.text?.toString().orEmpty()
        val passOk = passText.length >= 8
        val confirmOk = binding.etConfirmPassword.text?.toString().orEmpty() == passText && binding.etConfirmPassword.text?.isNotEmpty() == true
        val firstOk = binding.etFirstName.text?.toString().orEmpty().isNotBlank()
        val lastOk = binding.etLastName.text?.toString().orEmpty().isNotBlank()
        val phoneText = binding.etPhone.text?.toString().orEmpty()
        val phoneOk = phoneText.length == 10 && phoneText.all { it.isDigit() }
        val genderOk = binding.spGender.text?.toString().orEmpty().isNotBlank()
        binding.btnRegister.isEnabled = usernameOk && emailOk && passOk && confirmOk && firstOk && lastOk && phoneOk && genderOk
    }
}
