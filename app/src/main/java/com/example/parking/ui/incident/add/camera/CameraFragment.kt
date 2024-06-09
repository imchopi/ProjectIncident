package com.example.parking.ui.incident.add.camera

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.parking.R
import com.example.parking.databinding.FragmentCameraBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * A Fragment that handles camera operations, including capturing photos and requesting permissions.
 */
class CameraFragment : Fragment() {

    // View binding for the fragment layout
    private lateinit var viewBinding: FragmentCameraBinding

    // Image capture use case
    private var imageCapture: ImageCapture? = null

    // Executor service for running camera operations
    private lateinit var cameraExecutor: ExecutorService

    /**
     * Called to create the fragment's view hierarchy.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentCameraBinding.inflate(
            inflater,
            container,
            false
        )
        return viewBinding.root
    }

    /**
     * Called immediately after onCreateView() has returned.
     *
     * @param view The View returned by onCreateView().
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check for camera permissions and start camera if granted
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        // Set click listener for taking a picture
        viewBinding.takePicture.setOnClickListener { takePhoto() }

        // Initialize the camera executor service
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    /**
     * Function to capture a photo.
     */
    @RequiresApi(Build.VERSION_CODES.P)
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(requireContext().contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        imageCapture.takePicture(
            outputOptions,
            requireContext().mainExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.d(TAG, getString(R.string.photoCaptureFailed), exc)
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(CameraFragmentDirections.actionCameraFragmentToAddIncidentFragment())
                }
            }
        )
    }

    /**
     * Function to start the camera.
     */
    @RequiresApi(Build.VERSION_CODES.P)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.cameraPreview.surfaceProvider)
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            imageCapture = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, imageCapture, preview)
            } catch(exc: Exception) {
                Log.d(TAG, getString(R.string.bindingFailed), exc)
            }
        }, requireContext().mainExecutor)
    }

    /**
     * Function to handle permission request results.
     *
     * @param requestCode The request code passed to requestPermissions().
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.permissionsUser),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Function to check if all required permissions are granted.
     *
     * @return True if all permissions are granted, false otherwise.
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Function to clean up resources when the fragment is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    // Companion object with constant values
    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}
