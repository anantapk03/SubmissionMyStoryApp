package com.dicoding.submissionmystoryapp.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.submissionmystoryapp.ResultState
import com.dicoding.submissionmystoryapp.data.remote.response.DetailStoryResponse
import com.dicoding.submissionmystoryapp.databinding.ActivityDetailStoryBinding
import com.dicoding.submissionmystoryapp.view.login.LoginViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var detailStoryBinding: ActivityDetailStoryBinding

    private val detailViewModel by viewModels<MainUserViewModel> {
        LoginViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailStoryBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(detailStoryBinding.root)
        supportActionBar?.hide()

        val id = intent.getStringExtra("ID").toString()

        setDetailStoryResponse(id)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            detailStoryBinding.ProgressBar.visibility = View.VISIBLE
        } else {
            detailStoryBinding.ProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setDetailStory(detailStory:DetailStoryResponse){
        Glide.with(this)
            .load(detailStory.story?.photoUrl)
            .into(detailStoryBinding.photoStory)
        detailStoryBinding.tittleStory.text = detailStory.story?.name
        detailStoryBinding.detailStory.text = detailStory.story?.description
    }

    private fun setDetailStoryResponse(id : String){
        detailViewModel.getDetailStory(id).observe(this){result ->
            if(result != null){
                when(result){
                    is ResultState.Loading -> {
                        showLoading(true)
                    }
                    is ResultState.Success -> {
                        setDetailStory(result.data)
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