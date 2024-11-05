package com.blog.app.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blog.app.R
import com.blog.app.databinding.ActivityMainBinding
import com.blog.app.model.data.getPost.PostsDataResponseItem
import com.blog.app.model.data.getUsers.Data
import com.blog.app.viewmodel.PostsViewModel
import com.blog.app.model.handleResponse.Resource
import com.blog.app.presentation.adapter.PostListAdapter
import com.blog.app.presentation.adapter.UsersListAdapter
import com.blog.app.util.SharedPrefs
import com.blog.app.viewmodel.UsersViewModel

class MainActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var binding: ActivityMainBinding
    private lateinit var postsViewModel: PostsViewModel
    private lateinit var usersViewModel: UsersViewModel
    private lateinit var postList: MutableList<PostsDataResponseItem>
    private lateinit var usersList: MutableList<Data>
    private lateinit var postListAdapter: PostListAdapter
    private lateinit var usersListAdapter: UsersListAdapter
    private var recyclerViewState: Parcelable? = null
    private var recyclerViewState2: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        postsViewModel = ViewModelProvider(this)[PostsViewModel::class.java]
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

        postList = arrayListOf()
        usersList = arrayListOf()

        binding.postList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        binding.postList.setHasFixedSize(false)
        postListAdapter = PostListAdapter(this, postList)

        binding.usersList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
        binding.usersList.setHasFixedSize(false)
        usersListAdapter = UsersListAdapter(this, usersList)

        binding.postList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerViewState = recyclerView.layoutManager!!.onSaveInstanceState()!!
            }
        })

        binding.usersList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerViewState2 = recyclerView.layoutManager!!.onSaveInstanceState()!!
            }
        })

        binding.enterPost.setOnClickListener(this)
        binding.postNow.setOnClickListener(this)
        binding.userAvatar.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.enterPost -> {
                navigateToCreateNewPost()
            }

            R.id.postNow -> {
                navigateToCreateNewPost()
            }

            R.id.userAvatar -> {
                logoutDialog()
            }
        }
    }

    private fun navigateToCreateNewPost(){
        val intent = Intent(this@MainActivity, CreatePostActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getPostsList() {
        try {
            postList.clear()
            binding.progressBar.visibility = View.VISIBLE

            postsViewModel.getPostsList()

            postsViewModel.getPostsResponse.observe(this) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let {
                                try {
                                    postList.addAll(it)
                                    postListAdapter.setData(postList)
                                    binding.postList.adapter = postListAdapter
                                    postListAdapter.notifyDataSetChanged()
                                    binding.postList.layoutManager!!.onRestoreInstanceState(recyclerViewState)

                                    binding.progressBar.visibility = View.GONE

                                } catch (e: Exception){
                                    Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_LONG).show()
                                    binding.progressBar.visibility = View.GONE
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let {
                                binding.progressBar.visibility = View.GONE
                            }
                        }

                        is Resource.Loading -> {
                            // stop loader
                        }
                    }
                }
            }

        } catch (e: Exception){
            Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_LONG).show()
            binding.progressBar.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getUsersList(){
        try {
            usersList.clear()
            binding.progressBar.visibility = View.VISIBLE

            usersViewModel.getUsersList()

            usersViewModel.getUsersResponse.observe(this) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let {
                                try {
                                    usersList.addAll(it.data)
                                    usersListAdapter.setData(usersList)
                                    binding.usersList.adapter = usersListAdapter
                                    usersListAdapter.notifyDataSetChanged()
                                    binding.usersList.layoutManager!!.onRestoreInstanceState(recyclerViewState2)

                                    binding.progressBar.visibility = View.GONE

                                } catch (e: Exception){
                                    Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_LONG).show()
                                    binding.progressBar.visibility = View.GONE
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let {
                                binding.progressBar.visibility = View.GONE
                            }
                        }

                        is Resource.Loading -> {
                            // stop loader
                        }
                    }
                }
            }

        } catch (e: Exception){
            Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_LONG).show()
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun logoutDialog(){
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        alertDialogBuilder.setTitle("Logout !")
        alertDialogBuilder.setMessage("Are you sure to logout from app?")
        alertDialogBuilder.setCancelable(true)

        alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            sharedPrefs.write("authConfirmKey", "")
            sharedPrefs.write("authEmailKey", "")

            dialog.dismiss()
            onBackPressedDispatcher.onBackPressed()
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onResume() {
        getUsersList()
        getPostsList()
        super.onResume()
    }
}
