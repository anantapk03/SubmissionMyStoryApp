package com.dicoding.submissionmystoryapp.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.submissionmystoryapp.data.RegisterModel
import com.dicoding.submissionmystoryapp.data.StoryRepository

class SignUpViewModel (private val repository: StoryRepository) : ViewModel() {

    var registerModel = RegisterModel ( "default_Email", "defaul_password", "default_name")

    fun registerUser() = repository.registerUser(registerModel)
}