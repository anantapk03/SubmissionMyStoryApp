package com.dicoding.submissionmystoryapp.view.main

import androidx.lifecycle.ViewModel
import com.dicoding.submissionmystoryapp.data.StoryRepository
import java.io.File

class AddStoryViewModel (private val repository: StoryRepository) : ViewModel() {

    fun uploadImage(file: File, description: String) = repository.postStory(file, description)

}