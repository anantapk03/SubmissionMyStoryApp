package com.dicoding.submissionmystoryapp.data

import androidx.lifecycle.liveData
import com.dicoding.submissionmystoryapp.ResultState
import com.dicoding.submissionmystoryapp.data.remote.response.*
import com.dicoding.submissionmystoryapp.data.remote.retrofit.ApiConfig
import com.dicoding.submissionmystoryapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val settingPreferences: SettingPreferences
){

    fun registerUser(user : RegisterModel) = liveData<ResultState<RegisterResponse>> {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(user.name, user.email, user.password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }

    fun loginUser(email : String, password : String) = liveData<ResultState<LoginResponse>> {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }

    fun getSession() : Flow<UserModel>{
        return settingPreferences.getSession()
    }

    suspend fun saveSession(user: UserModel){
        settingPreferences.saveSession(user)
    }

    suspend fun logout(){
        settingPreferences.logout()
    }

    fun getStory() = liveData<ResultState<StoryResponse>> {
        emit(ResultState.Loading)
        try {
            settingPreferences.getSession()
            val user = runBlocking { settingPreferences.getSession().first() }
            val response = ApiConfig.getApiService(user.token)
            val successGetStories = response.getStories()
            emit(ResultState.Success(successGetStories))
        } catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }

    fun getDetailStory (id : String)= liveData<ResultState<DetailStoryResponse>> {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getDetailStory(id)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DetailStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }

    fun postStory(imageFile: File, description: String) = liveData<ResultState<AddStoryResponse>>{
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            settingPreferences.getSession()
            val user = runBlocking { settingPreferences.getSession().first() }
            val response = ApiConfig.getApiService(user.token)
            val successPostStory = response.uploadStory(multipartBody, requestBody)
            emit(ResultState.Success(successPostStory))
        } catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }

    }

    companion object{
        @Volatile
        private var instance : StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            settingPreferences: SettingPreferences
        ):StoryRepository =
            instance ?: synchronized(this){
                instance ?: StoryRepository(apiService, settingPreferences)
            }.also { instance = it }
    }
}