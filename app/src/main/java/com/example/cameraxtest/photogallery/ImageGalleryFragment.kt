package com.example.cameraxtest.photogallery

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cameraxtest.GalleryActivity
import com.example.cameraxtest.R
import com.example.cameraxtest.databinding.ActivityGalleryBinding
import com.example.cameraxtest.databinding.FragmentImageGalleryBinding


class ImageGalleryFragment : Fragment() {
    private var allPictures: ArrayList<Image>? = null
    private var _binding: FragmentImageGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageGalleryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasRequiredPermissions()){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(GalleryActivity.PERMISSION),
                GalleryActivity.REQUEST_PERMISSION_CODE
            )
        }
        allPictures = getAllImages()
        binding.imageRecycler.layoutManager = GridLayoutManager(requireContext(),3)
        binding.imageRecycler.setHasFixedSize(true)
        binding.imageRecycler.adapter = ImageAdapter(requireActivity(), allPictures!!)
    }

    private fun getAllImages(): ArrayList<Image>? {
        val images = ArrayList<Image>()
        val uris = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME)
        var cursor = requireActivity().contentResolver.query(uris,projection,null,null,null)
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
            requireContext(),
            GalleryActivity.PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}