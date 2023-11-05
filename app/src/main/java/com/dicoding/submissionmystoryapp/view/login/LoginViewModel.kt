package com.dicoding.submissionmystoryapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.submissionmystoryapp.data.StoryRepository
import com.dicoding.submissionmystoryapp.data.UserModel
import kotlinx.coroutines.launch

class LoginViewModel (private val repository: StoryRepository) : ViewModel() {

    var email : String = "Default_email"
    var password : String = "Default_Password"

    //Proses Login API
    fun loginUser() = repository.loginUser(email, password)

    //Proses DataStore
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }



}