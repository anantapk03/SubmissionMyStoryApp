package com.dicoding.submissionmystoryapp.view.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dicoding.submissionmystoryapp.R
import com.dicoding.submissionmystoryapp.databinding.ActivityWelcomeBinding
import com.dicoding.submissionmystoryapp.view.login.LoginActivity
import com.dicoding.submissionmystoryapp.view.signup.SignUpActivity

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var welcomeBinding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomeBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(welcomeBinding.root)
        supportActionBar?.hide()

        welcomeBinding.loginButton.setOnClickListener(this)
        welcomeBinding.signupButton.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.loginButton -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.signupButton -> startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}