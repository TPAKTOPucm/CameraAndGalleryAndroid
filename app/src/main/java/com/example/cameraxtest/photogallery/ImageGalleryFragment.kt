package com.example.cameraxtest.photogallery

import android.content.pm.PackageManager
import android.content.res.Resources
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
import com.example.cameraxtest.videogallery.VideoGalleryFragment


class ImageGalleryFragment : Fragment() {
    private var allPictures: ArrayList<String>? = null
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
        val displayMetrics = Resources.getSystem().displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        binding.imageRecycler.layoutManager = GridLayoutManager(requireContext(),(dpWidth/104).toInt())
        binding.imageRecycler.setHasFixedSize(true)
        binding.imageRecycler.adapter = ImageAdapter(requireActivity(), allPictures!!)
        binding.button.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.galleryFragmentContainerView,VideoGalleryFragment())
                .setReorderingAllowed(true)
                .commit()
        }
    }

    private fun getAllImages(): ArrayList<String> {
        val images = ArrayList<String>()
        val uris = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
        val cursor = requireActivity().contentResolver.query(uris,projection,null,null,null)
        try {
            cursor!!.moveToFirst()
            do {
                images.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)))
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