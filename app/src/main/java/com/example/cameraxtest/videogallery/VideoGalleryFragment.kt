package com.example.cameraxtest.videogallery

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
import com.example.cameraxtest.databinding.FragmentImageGalleryBinding
import com.example.cameraxtest.databinding.FragmentVideoGalleryBinding
import com.example.cameraxtest.photogallery.ImageGalleryFragment


class VideoGalleryFragment : Fragment() {
    private var allPictures: ArrayList<String>? = null
    private var _binding: FragmentVideoGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoGalleryBinding.inflate(inflater,container,false)
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
        binding.imageRecycler.adapter = VideoAdapter(requireActivity(), allPictures!!)
        binding.button.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.galleryFragmentContainerView, ImageGalleryFragment())
                .setReorderingAllowed(true)
                .commit()
        }
    }

    private fun getAllImages(): ArrayList<String>? {
        val images = ArrayList<String>()
        val uris = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Video.VideoColumns.DATA)
        var cursor = requireActivity().contentResolver.query(uris,projection,null,null,null)
        try {
            cursor!!.moveToFirst()
            do {
                images.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA)))
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