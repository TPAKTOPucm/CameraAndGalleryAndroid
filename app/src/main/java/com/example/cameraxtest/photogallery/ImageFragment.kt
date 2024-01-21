package com.example.cameraxtest.photogallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cameraxtest.R
import com.example.cameraxtest.databinding.FragmentImageBinding

class ImageFragment() : Fragment() {
    var imagePath: String? = null
    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    constructor(imagePath: String?) : this() {
        this.imagePath = imagePath
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setImageURI(Uri.parse(imagePath))
        binding.button.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.galleryFragmentContainerView,ImageGalleryFragment())
                .setReorderingAllowed(true)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}