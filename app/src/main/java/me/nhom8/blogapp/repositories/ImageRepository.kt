package me.nhom8.blogapp.repositories

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nhom8.blogapp.data.remote.ApiService
import me.nhom8.blogapp.data.remote.model.body.image.SaveImageBody
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
class ImageRepository
@Inject
constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context)
{

    private val TAG = "ImageRepository"

    // =========================
    // UPLOAD IMAGE (Cloudinary Unsigned)
    // =========================
    suspend fun uploadImage(filePath: String): String = withContext(Dispatchers.IO) {

        Log.d(TAG, "Start uploading image: $filePath")

        val file = File(filePath)

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, file.asRequestBody("image/*".toMediaType()))
            .addFormDataPart("upload_preset", "blog_unsigned") // unsigned preset
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/dhemdn3up/image/upload")
            .post(requestBody)
            .build()

        val response = OkHttpClient().newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("Upload failed: ${response.message}")
        }

        val json = JSONObject(response.body!!.string())
        val imageUrl = json.getString("secure_url")

        Log.d(TAG, "Cloudinary URL = $imageUrl")

        imageUrl
    }

    // =========================
    // SAVE IMAGE URL TO BACKEND
    // =========================
    suspend fun saveImageUrl(imageUrl: String) = withContext(Dispatchers.IO) {
        try {
            apiService.saveImageUrl(SaveImageBody(imageUrl = imageUrl))
            Log.d(TAG, "Image URL saved to backend successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save image URL: ${e.message}", e)
            throw e
        }
    }

    // =========================
    // HELPER: Uri → File
    // =========================
    fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Cannot open input stream")

        val file = File.createTempFile("upload_", ".jpg", context.cacheDir)
        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }

        return file
    }
}
