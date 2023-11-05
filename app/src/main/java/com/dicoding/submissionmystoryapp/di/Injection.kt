package com.dicoding.submissionmystoryapp.di

import android.content.Context
import com.dicoding.submissionmystoryapp.data.SettingPreferences
import com.dicoding.submissionmystoryapp.data.StoryRepository
import com.dicoding.submissionmystoryapp.data.dataStore
import com.dicoding.submissionmystoryapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val settingPreferences = SettingPreferences.getInstance(context.dataStore)
        val user = runBlocking {
            settingPreferences.getSession().first()
        }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, settingPreferences)
    }

}