package com.dicoding.submissionmystoryapp.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.submissionmystoryapp.ResultState
import com.dicoding.submissionmystoryapp.data.UserModel
import com.dicoding.submissionmystoryapp.databinding.ActivityLoginBinding
import com.dicoding.submissionmystoryapp.view.main.MainActivity
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding


    private val factory = LoginViewModelFactory.getInstance(this)
    private val loginViewModel by viewModels<LoginViewModel> {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        supportActionBar?.hide()

        loginBinding.myEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyEmail(p0)
            }

            override fun afterTextChanged(p0: Editable) {

            }
        })

        loginBinding.loginButton.setOnClickListener {
            loginViewModel.email = loginBinding.myEmail.text.toString()
            loginViewModel.password = loginBinding.passwordEditText.text.toString()
            postLogin()
        }
    }

    private fun setMyEmail(result: CharSequence){
        if(result.isNotEmpty()){
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
            if (result.matches(emailRegex.toRegex())){
                loginBinding.myEmail.error = null
            } else {
                loginBinding.myEmail.error = "Masukan Email dengan benar!"
            }
        } else{
            loginBinding.myEmail.error = null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading (isLoading : Boolean){
        loginBinding.ProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun postLogin(){
        loginViewModel.loginUser().observe(this){ result ->
            if(result != null) {
                when(result){
                    is ResultState.Loading ->{
                        showLoading(true)
                    }
                    is ResultState.Success -> {
                        val email = loginBinding.myEmail.text.toString()
                        val token = result.data.loginResult?.token.toString()
                        loginViewModel.saveSession(UserModel(email, token))
                        showLoading(false)
                        showToast(result.data.message.toString())
                        //Intent to MainActivity
                        AlertDialog.Builder(this).apply {
                            setTitle("Selamat Datang, ${result.data.loginResult?.name.toString()}!")
                            setMessage("Anda berhasil login")
                            setPositiveButton("Lanjut"){_, _ ->
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                    is ResultState.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }
        }
    }


}