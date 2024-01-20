package com.example.cameraxtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        binding.galleryButton.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
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