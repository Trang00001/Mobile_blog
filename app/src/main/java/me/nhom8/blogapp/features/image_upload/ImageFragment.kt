package me.nhom8.blogapp.features.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.nhom8.blogapp.R
import me.nhom8.blogapp.databinding.FragmentImageBinding
import me.nhom8.blogapp.features.helpers.LoadingStatus
import me.nhom8.blogapp.features.image_upload.ImageUploadViewModel
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class ImageFragment : Fragment(R.layout.fragment_image) {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ImageUploadViewModel by viewModels()

    private val TAG = "ImageFragment"

    companion object {
        const val RESULT_KEY_IMAGE = "RESULT_KEY_IMAGE"
        const val BUNDLE_KEY_IMAGE_URL = "BUNDLE_KEY_IMAGE_URL"

        private const val ARG_SHOW_BUTTON = "ARG_SHOW_BUTTON"

        fun newInstance(showButton: Boolean = true): ImageFragment {
            return ImageFragment().apply {
                arguments = bundleOf(ARG_SHOW_BUTTON to showButton)
            }
        }
    }

    private fun shouldShowButton(): Boolean {
        return arguments?.getBoolean(ARG_SHOW_BUTTON, true) ?: true
    }

    // STEP 1: picker
    private val pickImageLauncher =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Log.d(TAG, "Image picked: $it")
                uploadFromUri(it)
            } ?: Log.d(TAG, "No image selected")
        }

    /**
     * Cho phép parent Fragment gọi trực tiếp để mở gallery.
     */
    fun openImagePicker() {
        pickImageLauncher.launch("image/*")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentImageBinding.bind(view)

        // Nếu showButton = false thì fragment chỉ đóng vai trò "uploader" ẩn,
        // parent sẽ có button riêng.
        binding.btnUpload.visibility = if (shouldShowButton()) View.VISIBLE else View.GONE

        binding.btnUpload.setOnClickListener {
            Log.d(TAG, "Upload button clicked")
            openImagePicker()
        }

        observeState()
    }

    private fun uploadFromUri(uri: Uri) {
        lifecycleScope.launch {
            try {
                Log.d(TAG, "Starting upload for URI: $uri")

                // 1️⃣ decode kích thước ảnh trước
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                requireContext().contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it, null, options)
                }

                Log.d(TAG, "Original image size: ${options.outWidth}x${options.outHeight}")

                // 2️⃣ tính scale max 1024px
                val maxDim = 1024
                var scale = 1
                while (options.outWidth / scale > maxDim || options.outHeight / scale > maxDim) scale *= 2

                // 3️⃣ decode bitmap với scale
                val resizeOptions = BitmapFactory.Options().apply { inSampleSize = scale }
                val bitmap: Bitmap? = requireContext().contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it, null, resizeOptions)
                }

                if (bitmap == null) {
                    Toast.makeText(requireContext(), "Không thể đọc ảnh", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                Log.d(TAG, "Resized bitmap size: ${bitmap.width}x${bitmap.height}")

                // 4️⃣ lưu bitmap vào file tạm
                val tempFile = File(requireContext().cacheDir, "upload_image.jpg")
                FileOutputStream(tempFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
                }
                bitmap.recycle()
                Log.d(TAG, "Bitmap saved to: ${tempFile.absolutePath}")

                // 5️⃣ gọi ViewModel upload
                viewModel.uploadImage(tempFile.absolutePath)
                Log.d(TAG, "Upload triggered")

            } catch (e: Exception) {
                Log.e(TAG, "Upload failed: ${e.message}", e)
                Toast.makeText(requireContext(), "Upload thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state.loadingStatus) {
                    LoadingStatus.LOADING -> binding.progressBar.visibility = View.VISIBLE

                    LoadingStatus.DONE -> {
                        binding.progressBar.visibility = View.GONE
                        state.imageUrl?.let { url ->
                            Log.d(TAG, "Image URL: $url")

                            // Trả URL về parent (SubmitBlogInfoFragment) để gắn vào blog payload
                            setFragmentResult(
                                RESULT_KEY_IMAGE,
                                bundleOf(BUNDLE_KEY_IMAGE_URL to url),
                            )

                            if (shouldShowButton()) {
                                Toast.makeText(requireContext(), "Upload thành công", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    LoadingStatus.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            state.error?.message ?: "Upload thất bại",
                            Toast.LENGTH_SHORT,
                        ).show()
                        Log.e(TAG, "Upload error: ${state.error?.message}")
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
