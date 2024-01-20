package com.example.cameraxtest.videogallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cameraxtest.R
import com.example.cameraxtest.databinding.FragmentImageBinding
import com.example.cameraxtest.databinding.FragmentVideoPlayBinding

class VideoFragment() : Fragment() {
    var imagePath: String? = null
    private var _binding: FragmentVideoPlayBinding? = null
    private val binding get() = _binding!!
    constructor(imagePath: String?) : this() {
        this.imagePath = imagePath
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mediaController = MediaController(requireContext())
        binding.videoView.setMediaController(mediaController)
        binding.videoView.setVideoURI(Uri.parse(imagePath))
        binding.videoView.requestFocus()
        binding.videoView.start()

        binding.button.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.galleryFragmentContainerView,VideoGalleryFragment())
                .setReorderingAllowed(true)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}