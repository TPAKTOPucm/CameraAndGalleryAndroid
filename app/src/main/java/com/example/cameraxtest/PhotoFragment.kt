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
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cameraxtest.databinding.FragmentPhotoBinding
import java.io.File
import java.text.SimpleDateFormat

class PhotoFragment : Fragment() {
    private var imageCapture: ImageCapture? = null
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
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
        binding.photo.setOnClickListener {
            if (imageCapture == null)
                return@setOnClickListener
            val photoDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            if (photoDir.canWrite()) {
                val file = File(
                    photoDir,
                    SimpleDateFormat(MainActivity.DATETIME_FORMAT).format(System.currentTimeMillis()) + ".jpg"
                )
                val outputOption = ImageCapture.OutputFileOptions.Builder(file).build()
                imageCapture!!.takePicture(
                    outputOption,
                    ContextCompat.getMainExecutor(requireContext()),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            Toast.makeText(context, "gg", Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e("error while saving image", exception.stackTraceToString())
                        }
                    })
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
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception){
                Log.e("camera", "can not start camera", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }
}