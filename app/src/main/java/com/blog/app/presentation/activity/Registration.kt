package com.blog.app.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.blog.app.R
import com.blog.app.databinding.ActivityRegistrationBinding
import com.blog.app.model.data.auth.AuthRequest
import com.blog.app.model.handleResponse.Resource
import com.blog.app.util.RegexText
import com.blog.app.util.SharedPrefs
import com.blog.app.viewmodel.UsersViewModel
import com.bumptech.glide.Glide

class Registration: AppCompatActivity(), View.OnClickListener {

    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var usersViewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usersViewModel = ViewModelProvider(this)[UsersViewModel::class.java]

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )

        } else {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        sharedPrefs = SharedPrefs()
        sharedPrefs.init(this)

        binding.enterEmailAddress.setText(resources.getString(R.string.sample_email))

        binding.enterPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable) {
                if(p0.toString().isNotEmpty() && p0.toString()!="null"){
                    val hasRegex = RegexText.checkNumberUppercaseLowercaseSpecialCharacters(p0.toString())

                    if(hasRegex){
                        binding.passwordRegexAlert.visibility = View.GONE
                    }
                }
            }

        })

        binding.register.setOnClickListener(this)
        binding.male.setOnClickListener(this)
        binding.female.setOnClickListener(this)
        binding.loginPage.setOnClickListener(this)

        checkPreviousLogin()
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.loginPage -> {
                val intent = Intent(this@Registration, Signin::class.java)
                startActivity(intent)
            }

            R.id.male -> {
                Glide.with(this).load(R.drawable.baseline_male_24).into(binding.selectedGenderImage)
            }

            R.id.female -> {
                Glide.with(this).load(R.drawable.baseline_female_24).into(binding.selectedGenderImage)
            }

            R.id.register -> {
                val email = binding.enterEmailAddress.text.toString()
                val userName = binding.enterUsername.text.toString()
                val password = binding.enterPassword.text.toString()
                val hasRegex = RegexText.checkNumberUppercaseLowercaseSpecialCharacters(password)
                val dob = binding.enterDob.text.toString()

                if(email.isEmpty()){
                    binding.enterEmailAddress.error = resources.getString(R.string.your_email)

                } else if(userName.isEmpty()){
                    binding.enterUsername.error = resources.getString(R.string.your_name)

                } else if(password.isEmpty()){
                    binding.enterPassword.error = resources.getString(R.string.create_password)

                } else if(!hasRegex){
                    binding.passwordRegexAlert.visibility = View.VISIBLE

                } else if(dob.isEmpty()){
                    binding.enterDob.error = resources.getString(R.string.date_of_birth)

                } else {
                    registerUser(email, password)
                }
            }
        }
    }

    private fun checkPreviousLogin() {
        var loginLog = ""

        try {
            loginLog = sharedPrefs.read("authConfirmKey", "").toString()

        } catch (e: Exception){
            e.printStackTrace()
        }

        if(loginLog.isNotEmpty()){
            navigateToFeedPage()
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun registerUser(email: String, password: String){
        try {
            binding.register.text = ""
            binding.progressBar.visibility = View.VISIBLE
            binding.register.isClickable = false
            binding.register.isEnabled = false

            val authRequest = AuthRequest(email, password)
            usersViewModel.registerUser(authRequest)

            usersViewModel.registerResponse.observe(this) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let {
                                try {
                                    sharedPrefs.write("authConfirmKey", "yes")
                                    sharedPrefs.write("authEmailKey", email)

                                    binding.register.text = resources.getString(R.string.sign_up)
                                    binding.progressBar.visibility = View.GONE
                                    binding.register.isClickable = true
                                    binding.register.isEnabled = true

                                    navigateToFeedPage()

                                } catch (e: Exception){
                                    Toast.makeText(this@Registration, "${e.message}", Toast.LENGTH_LONG).show()
                                    binding.register.text = resources.getString(R.string.sign_up)
                                    binding.progressBar.visibility = View.GONE
                                    binding.register.isClickable = true
                                    binding.register.isEnabled = true
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let {
                                Toast.makeText(this@Registration, it, Toast.LENGTH_LONG).show()
                                binding.register.text = resources.getString(R.string.sign_up)
                                binding.progressBar.visibility = View.GONE
                                binding.register.isClickable = true
                                binding.register.isEnabled = true
                            }
                        }

                        is Resource.Loading -> {
                            // stop loader
                        }
                    }
                }
            }

        } catch (e: Exception){
            Toast.makeText(this@Registration, "${e.message}", Toast.LENGTH_LONG).show()
            binding.register.text = resources.getString(R.string.sign_up)
            binding.progressBar.visibility = View.GONE
            binding.register.isClickable = true
            binding.register.isEnabled = true
        }
    }

    private fun navigateToFeedPage(){
        val intent = Intent(this@Registration, MainActivity::class.java)
        startActivity(intent)
    }
}
