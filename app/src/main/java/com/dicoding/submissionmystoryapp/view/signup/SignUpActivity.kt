package com.dicoding.submissionmystoryapp.view.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.submissionmystoryapp.ResultState
import com.dicoding.submissionmystoryapp.databinding.ActivitySignUpBinding
import com.dicoding.submissionmystoryapp.view.login.LoginActivity

class SignUpActivity : AppCompatActivity(){

    //Inisiasi ViewBinding
    private lateinit var signUpBinding: ActivitySignUpBinding

    //ViewModel and ViewModel Factory
    private val factory = SignUpViewModelFactory.getInstance(this)
    private val registerViewModel by viewModels<SignUpViewModel> {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpBinding.root)
        supportActionBar?.hide()

        //TEXT EDIT EMAIL
        signUpBinding.emailEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyEmail(p0)
            }

            override fun afterTextChanged(p0: Editable) {
//                TODO("Not yet implemented")
            }

        })

        //ON CLICK BUTTON POST
        signUpBinding.signupButton.setOnClickListener {

            //Inisiasi name, email, and password for Form to Post in API
            registerViewModel.registerModel.email = signUpBinding.emailEditText.text.toString()
            registerViewModel.registerModel.name = signUpBinding.nameEditText.text.toString()
            registerViewModel.registerModel.password = signUpBinding.passwordEditText.text.toString()

            //Post Data
            registerViewModel.registerUser().observe(this){ result ->
                if(result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                        is ResultState.Success -> {
                            showToast(result.data.message.toString(), signUpBinding.emailEditText.text.toString())
                            showLoading(false)
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        is ResultState.Error -> {
                            showToast(result.error , signUpBinding.emailEditText.text.toString())
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }
    //Edit Text Email
    private fun setMyEmail(result: CharSequence){
        //val test = EMAIL_ADDRESS_PATTERN.matcher(result).mat
        if(result.isNotEmpty()){
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
            if (result.matches(emailRegex.toRegex())){
                signUpBinding.emailEditText.error = null
            } else {
                signUpBinding.emailEditText.error = "Masukan Email dengan benar!"
            }
        } else{
            signUpBinding.emailEditText.error = null
        }
    }

    //Show Message Respond
    private fun showToast(message: String, email : String) {
        Toast.makeText(this, "Email : $email ${message}", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading (isLoading : Boolean){
        signUpBinding.ProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}