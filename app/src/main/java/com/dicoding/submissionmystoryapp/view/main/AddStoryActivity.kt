package com.dicoding.submissionmystoryapp.view.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dicoding.submissionmystoryapp.ResultState
import com.dicoding.submissionmystoryapp.databinding.ActivityAddStoryBinding
import com.dicoding.submissionmystoryapp.databinding.ActivityDetailStoryBinding
import com.dicoding.submissionmystoryapp.view.login.LoginViewModelFactory
import com.dicoding.submissionmystoryapp.view.welcome.WelcomeActivity

class AddStoryActivity : AppCompatActivity() {

    private lateinit var addStoryBinding: ActivityAddStoryBinding

    private var currenImageUri : Uri? = null

    private val mainViewModel by viewModels<MainUserViewModel> {
        LoginViewModelFactory.getInstance(this)
    }

    private val addStoryViewModel by viewModels<AddStoryViewModel> {
        LoginViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addStoryBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(addStoryBinding.root)

        mainViewModel.getSession().observe(this){
            if(!it.isLogin){
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else{
                showToast("${it.email}")
            }
        }

        addStoryBinding.btnGallery.setOnClickListener { startGallery() }
        addStoryBinding.btnCamera.setOnClickListener { startCamera() }
        addStoryBinding.uploadButton.setOnClickListener {
            uploadStory()
        }
    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera(){
        currenImageUri = getImageUri(this)
        launcherIntentCamera.launch(currenImageUri)
    }

    private fun uploadStory(){
        currenImageUri?.let { uri ->
            val imageFile = uriToFile (uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = addStoryBinding.descriptionPicture.text.toString()


            addStoryViewModel.uploadImage(imageFile, description).observe(this){result ->
                if(result != null){
                    when (result){
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                        is ResultState.Success -> {
                            showToast(result.data.message.toString())
                            showLoading(false)
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        is ResultState.Error -> {
                            showToast(result.error)
                            showLoading(false)
                        }
                    }
                }
            }
        } ?: showToast("Masukan gambar dulu ya")
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){ isSuccess ->
        if (isSuccess){
            showImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){ uri: Uri? ->

        if(uri != null){
            currenImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage(){
        currenImageUri.let {
            Log.d("Image URI", "showImage: $it")
            addStoryBinding.previewImageView.setImageURI(it)
        }
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading (isLoading: Boolean){
        addStoryBinding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}