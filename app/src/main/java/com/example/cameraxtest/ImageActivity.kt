package com.example.cameraxtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val imagePath=intent.getStringExtra("path")
        val imageName=intent.getStringExtra("name")
        supportActionBar?.setTitle(imageName)
        Glide.with(this).load(imagePath).into(findViewById(R.id.imageView))
    }
}