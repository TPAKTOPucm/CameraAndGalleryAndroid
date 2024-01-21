package com.example.cameraxtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.cameraxtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    companion object {
        val CAMERAX_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO)
        const val DATETIME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val REQUEST_CAMERAX_PERMISSIONS_CODE = 1
        const val BUTTON_SWITCH_TO_VIDEO_TEXT = "Видео"
        const val BUTTON_SWITCH_TO_PHOTO_TEXT = "Фото"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        switchFragment(intent.getBooleanExtra("toPhoto", false))
        binding.galleryButton.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
        binding.switchFragment.setOnClickListener {
            switchFragment(binding.switchFragment.text == BUTTON_SWITCH_TO_PHOTO_TEXT)
        }
    }

    fun startCamera(surfaceProvider: SurfaceProvider, cameraSelector: CameraSelector, useCase: UseCase){
        val cameraProviderFeature = ProcessCameraProvider.getInstance(applicationContext)
        cameraProviderFeature.addListener({
            val cameraProvider = cameraProviderFeature.get() as ProcessCameraProvider
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(surfaceProvider)
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, useCase)
            } catch (e: Exception){
                Log.e("camera", "can not start camera", e)
            }
        }, ContextCompat.getMainExecutor(applicationContext))
    }

    private fun switchFragment(toPhoto: Boolean){
        val fragment = if (toPhoto){
            binding.switchFragment.text = BUTTON_SWITCH_TO_VIDEO_TEXT
            PhotoFragment()
        }
        else{
            binding.switchFragment.text = BUTTON_SWITCH_TO_PHOTO_TEXT
            VideoFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,fragment)
            .setReorderingAllowed(true)
            .commit()
    }
}