package com.dicoding.submissionmystoryapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionmystoryapp.R
import com.dicoding.submissionmystoryapp.ResultState
import com.dicoding.submissionmystoryapp.data.UserModel
import com.dicoding.submissionmystoryapp.data.remote.response.ListStoryItem
import com.dicoding.submissionmystoryapp.databinding.ActivityMainBinding
import com.dicoding.submissionmystoryapp.view.login.ListStoryAdapter
import com.dicoding.submissionmystoryapp.view.login.LoginViewModel
import com.dicoding.submissionmystoryapp.view.login.LoginViewModelFactory
import com.dicoding.submissionmystoryapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    private val userViewModel by viewModels<MainUserViewModel> {
        LoginViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val layOutManager = LinearLayoutManager(this)
        mainBinding.rvStory.layoutManager = layOutManager
        val itemDecoration = DividerItemDecoration(this, layOutManager.orientation)
        mainBinding.rvStory.addItemDecoration(itemDecoration)

        userViewModel.getSession().observe(this){user ->
            if(!user.isLogin){
                startActivity(Intent(this, WelcomeActivity::class.java))
                showToast("email : ${user.email}")
                finish()
            }
        }

        getStory()
        mainBinding.fbAddStory.setOnClickListener{
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            mainBinding.progressBar.visibility = View.VISIBLE
        } else {
            mainBinding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun setStory(listStoryItem: List<ListStoryItem?>?){
        val adapter = ListStoryAdapter()
        adapter.submitList(listStoryItem)
        mainBinding.rvStory.adapter = adapter
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getStory(){
        userViewModel.getSession().observe(this){
            if(it.isLogin){
                userViewModel.getStory().observe(this){result ->
                    if(result != null ){
                        when(result){
                            is ResultState.Loading -> {
                                showLoading(true)
                            }
                            is ResultState.Success -> {
                                setStory(result.data.listStory)
                                showLoading(false)
                            }
                            is ResultState.Error -> {
                                showLoading(false)
                                showToast(result.error)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_logout -> {
                userViewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}