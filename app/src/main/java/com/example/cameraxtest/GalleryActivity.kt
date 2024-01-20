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
import com.example.cameraxtest.databinding.ActivityMainBinding

class GalleryActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
        const val REQUEST_PERMISSION_CODE = 2
    }
    private lateinit var binding: ActivityGalleryBinding
    private var allPictures: ArrayList<Image>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!hasRequiredPermissions()){
            ActivityCompat.requestPermissions(this, arrayOf(PERMISSION), REQUEST_PERMISSION_CODE)
        }
        allPictures = getAllImages()
        binding.imageRecycler.layoutManager = GridLayoutManager(this,3)
        binding.imageRecycler.setHasFixedSize(true)
        binding.imageRecycler.adapter = ImageAdapter(this, allPictures!!)
    }

    private fun getAllImages(): ArrayList<Image>? {
        val images = ArrayList<Image>()
        val uris = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA,MediaStore.Images.Media.DISPLAY_NAME)
        var cursor = contentResolver.query(uris,projection,null,null,null)
        try {
            cursor!!.moveToFirst()
            do {
                val image = Image(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    )
                images.add(image)
            } while (cursor.moveToNext())
            cursor.close()
        } catch (e: Exception){
            Log.e("Exception", e.stackTraceToString())
        }
        return images
    }

    private fun hasRequiredPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
                applicationContext,
                PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
    }
}