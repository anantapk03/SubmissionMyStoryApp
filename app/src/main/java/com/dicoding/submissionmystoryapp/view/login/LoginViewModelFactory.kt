package com.dicoding.submissionmystoryapp.view.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submissionmystoryapp.data.StoryRepository
import com.dicoding.submissionmystoryapp.di.Injection
import com.dicoding.submissionmystoryapp.view.main.AddStoryViewModel
import com.dicoding.submissionmystoryapp.view.main.MainUserViewModel

class LoginViewModelFactory (private val repository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHEKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainUserViewModel::class.java) -> {
                MainUserViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) ->{
                AddStoryViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class : " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance : LoginViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context) =
            instance ?: synchronized(this){
                instance ?: LoginViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}