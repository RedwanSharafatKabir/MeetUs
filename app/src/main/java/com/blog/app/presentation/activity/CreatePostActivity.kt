package com.blog.app.presentation.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.blog.app.R
import com.blog.app.databinding.ActivityCreatePostBinding
import com.blog.app.model.data.createPost.CreatePostRequest
import com.blog.app.viewmodel.PostsViewModel
import com.blog.app.model.handleResponse.Resource
import com.bumptech.glide.Glide

class CreatePostActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCreatePostBinding
    private lateinit var postsViewModel: PostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        postsViewModel = ViewModelProvider(this)[PostsViewModel::class.java]

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

        binding.goToBackPage.setOnClickListener(this)
        binding.postNow.setOnClickListener(this)
        binding.uploadPhoto.setOnClickListener(this)
        binding.removeSelectedPhoto.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.goToBackPage -> {
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.uploadPhoto -> {
                val intent = Intent()
                intent.setType("image/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                individualImageResultLauncher.launch(intent)
            }

            R.id.removeSelectedPhoto -> {
                binding.uploadedPhoto.setImageDrawable(null)
                binding.photoLayout.visibility = View.GONE
            }

            R.id.postNow -> {
                val postTitle = "Sample Post Title"
                val postBody = binding.enterPostDetails.text.toString()

                if(postBody.isNotEmpty()){
                    cratePost(postTitle, postBody)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun cratePost(postTitle: String, postBody: String) {
        try {
            binding.postNow.text = ""
            binding.progressBar.visibility = View.VISIBLE
            binding.postNow.isClickable = false
            binding.postNow.isEnabled = false

            val createPostRequest = CreatePostRequest(postBody, postTitle, 1)
            postsViewModel.createPost(createPostRequest)

            postsViewModel.createPostsResponse.observe(this) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let {
                                try {
                                    Toast.makeText(this@CreatePostActivity, "Post created successfully.", Toast.LENGTH_LONG).show()
                                    onBackPressedDispatcher.onBackPressed()

                                } catch (e: Exception){
                                    Toast.makeText(this@CreatePostActivity, "${e.message}", Toast.LENGTH_LONG).show()
                                    binding.postNow.text = resources.getString(R.string.sign_in)
                                    binding.progressBar.visibility = View.GONE
                                    binding.postNow.isClickable = true
                                    binding.postNow.isEnabled = true
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let {
                                Toast.makeText(this@CreatePostActivity, it, Toast.LENGTH_LONG).show()
                                binding.postNow.text = resources.getString(R.string.sign_in)
                                binding.progressBar.visibility = View.GONE
                                binding.postNow.isClickable = true
                                binding.postNow.isEnabled = true
                            }
                        }

                        is Resource.Loading -> {
                            // stop loader
                        }
                    }
                }
            }

        } catch (e: Exception){
            Toast.makeText(this@CreatePostActivity, "${e.message}", Toast.LENGTH_LONG).show()
            binding.postNow.text = resources.getString(R.string.sign_in)
            binding.progressBar.visibility = View.GONE
            binding.postNow.isClickable = true
            binding.postNow.isEnabled = true
        }
    }

    private val individualImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val imageUri = result.data!!.data!!

                    binding.photoLayout.visibility = View.VISIBLE
                    Glide.with(this).load(imageUri).into(binding.uploadedPhoto)
                }

                else -> {
                    Log.i("task_error", "Task Cancelled")
                }
            }
        }
    
    @Deprecated("Deprecated in Java", ReplaceWith("onBackPressedDispatcher.onBackPressed()"))
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        super.onBackPressed()
    }
}
