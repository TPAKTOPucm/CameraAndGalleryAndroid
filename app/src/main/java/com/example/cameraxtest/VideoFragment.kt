package com.example.cameraxtest

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.impl.MutableOptionsBundle
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import com.example.cameraxtest.databinding.FragmentPhotoBinding
import com.example.cameraxtest.databinding.FragmentVideoBinding
import java.io.File
import java.text.SimpleDateFormat

class VideoFragment : Fragment() {
    private var recording: Recording? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.switchCamera.setOnClickListener {
            cameraSelector = if(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                CameraSelector.DEFAULT_FRONT_CAMERA
            else
                CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
        if(!hasRequiredPermissions())
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    MainActivity.CAMERAX_PERMISSIONS,
                    MainActivity.REQUEST_CAMERAX_PERMISSIONS_CODE
                )
            }
        else
            startCamera()
        binding.video.setOnClickListener {
            if (videoCapture == null)
                return@setOnClickListener
            if (recording != null){
                recording!!.stop()
                recording = null
                return@setOnClickListener
            }
            val videoDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            if (videoDir.canWrite()) {
                val file = File(
                    videoDir,
                    SimpleDateFormat(MainActivity.DATETIME_FORMAT).format(System.currentTimeMillis()) + ".mp4"
                )
                val outputOption = FileOutputOptions.Builder(file).build()
                recording = videoCapture!!.output.prepareRecording(requireContext(),outputOption).start(ContextCompat.getMainExecutor(requireContext())
                ) {  }
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return MainActivity.CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if(requestCode == MainActivity.REQUEST_CAMERAX_PERMISSIONS_CODE){
            if(hasRequiredPermissions()){
                startCamera()
            } else {
                Toast.makeText(activity, "Разрешения не получены", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera(){
        val cameraProviderFeature = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFeature.addListener({
            val cameraProvider = cameraProviderFeature.get() as ProcessCameraProvider
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            videoCapture = VideoCapture.withOutput(Recorder.Builder().build())

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector,preview,videoCapture)
            } catch (e: Exception){
                Log.e("camera", "can not start camera", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }
}