package com.dicoding.submissionmystoryapp.data

data class UserModel (
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)