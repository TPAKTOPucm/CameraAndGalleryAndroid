package com.example.cameraxtest.videogallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.example.cameraxtest.R
import com.example.cameraxtest.databinding.FragmentVideoPlayBinding

class VideoFragment() : Fragment() {
    var videoPath: String? = null
    private var _binding: FragmentVideoPlayBinding? = null
    private val binding get() = _binding!!
    constructor(path: String?) : this() {
        videoPath = path
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
        binding.videoView.setVideoURI(Uri.parse(videoPath))
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