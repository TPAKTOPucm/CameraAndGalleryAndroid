package com.example.cameraxtest

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.cameraxtest.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    companion object {
        val CAMERAX_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO)
        const val DATETIME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val REQUEST_CAMERAX_PERMISSIONS_CODE = 1
        const val BUTTON_SWITCH_TO_VIDEO_TEXT = "video"
        const val BUTTON_SWITCH_TO_PHOTO_TEXT = "photo"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.switchFragment.setOnClickListener {
            val fragment = if (binding.switchFragment.text == BUTTON_SWITCH_TO_PHOTO_TEXT){
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
}