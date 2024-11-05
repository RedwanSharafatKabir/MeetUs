package com.blog.app.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.blog.app.databinding.ActivitySigninBinding
import com.blog.app.model.data.auth.AuthRequest
import com.blog.app.model.handleResponse.Resource
import com.blog.app.util.SharedPrefs
import com.blog.app.viewmodel.UsersViewModel

class Signin: AppCompatActivity(), View.OnClickListener {

    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var binding: ActivitySigninBinding
    private lateinit var usersViewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
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

        binding.login.setOnClickListener(this)
        binding.forgotPassword.setOnClickListener(this)
        binding.signupPage.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.signupPage -> {
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.forgotPassword -> {
                val intent = Intent(this@Signin, ForgetPassword::class.java)
                startActivity(intent)
            }

            R.id.login -> {
                val email = binding.enterEmailAddress.text.toString()
                val password = binding.enterPassword.text.toString()

                if(email.isEmpty()){
                    binding.enterEmailAddress.error = resources.getString(R.string.your_email)

                } else if(password.isEmpty()){
                    binding.enterPassword.error = resources.getString(R.string.create_password)

                } else {
                    val rememberMe = binding.rememberMe.isChecked
                    loginUser(email, password, rememberMe)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun loginUser(email: String, password: String, rememberMe: Boolean){
        try {
            binding.login.text = ""
            binding.progressBar.visibility = View.VISIBLE
            binding.login.isClickable = false
            binding.login.isEnabled = false

            val authRequest = AuthRequest(email, password)
            usersViewModel.loginUser(authRequest)

            usersViewModel.loginResponse.observe(this) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let {
                                try {
                                    if(rememberMe){
                                        sharedPrefs.write("authConfirmKey", "yes")
                                        sharedPrefs.write("authEmailKey", email)
                                    }

                                    binding.login.text = resources.getString(R.string.sign_in)
                                    binding.progressBar.visibility = View.GONE
                                    binding.login.isClickable = true
                                    binding.login.isEnabled = true

                                    navigateToFeedPage()

                                } catch (e: Exception){
                                    Toast.makeText(this@Signin, "${e.message}", Toast.LENGTH_LONG).show()
                                    binding.login.text = resources.getString(R.string.sign_in)
                                    binding.progressBar.visibility = View.GONE
                                    binding.login.isClickable = true
                                    binding.login.isEnabled = true
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let {
                                Toast.makeText(this@Signin, it, Toast.LENGTH_LONG).show()
                                binding.login.text = resources.getString(R.string.sign_in)
                                binding.progressBar.visibility = View.GONE
                                binding.login.isClickable = true
                                binding.login.isEnabled = true
                            }
                        }

                        is Resource.Loading -> {
                            // stop loader
                        }
                    }
                }
            }

        } catch (e: Exception){
            Toast.makeText(this@Signin, "${e.message}", Toast.LENGTH_LONG).show()
            binding.login.text = resources.getString(R.string.sign_in)
            binding.progressBar.visibility = View.GONE
            binding.login.isClickable = true
            binding.login.isEnabled = true
        }
    }

    private fun navigateToFeedPage(){
        val intent = Intent(this@Signin, MainActivity::class.java)
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java", ReplaceWith("onBackPressedDispatcher.onBackPressed()"))
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        super.onBackPressed()
    }
}
