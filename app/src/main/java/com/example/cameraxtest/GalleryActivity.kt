package com.example.cameraxtest

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cameraxtest.databinding.ActivityGalleryBinding
import com.example.cameraxtest.photogallery.ImageAdapter

class GalleryActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
        const val REQUEST_PERMISSION_CODE = 2
    }
    private lateinit var binding: ActivityGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}