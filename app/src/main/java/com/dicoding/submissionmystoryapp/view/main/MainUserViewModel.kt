package com.dicoding.submissionmystoryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.submissionmystoryapp.data.StoryRepository
import com.dicoding.submissionmystoryapp.data.UserModel
import kotlinx.coroutines.launch

class MainUserViewModel (private val repository: StoryRepository) : ViewModel(){

    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession() : LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }

    fun getStory() = repository.getStory()

    fun getDetailStory(id: String) = repository.getDetailStory(id)
}