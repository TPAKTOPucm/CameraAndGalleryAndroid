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
import androidx.camera.lifecycle.ProcessCameraProvider
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
    private val activity get() = requireActivity() as MainActivity
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
                ActivityCompat.requestPermissions(
                    activity,
                    MainActivity.CAMERAX_PERMISSIONS,
                    MainActivity.REQUEST_CAMERAX_PERMISSIONS_CODE
                )
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
                            Toast.makeText(context, "Снимок сохранён", Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e("Ошибка при сохранении снимка", exception.stackTraceToString())
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
        imageCapture = ImageCapture.Builder().build()
        activity.startCamera(binding.previewView.surfaceProvider, cameraSelector, imageCapture!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}