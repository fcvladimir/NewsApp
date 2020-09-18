package com.test.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.test.data.datasources.IImageDS
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream

class RemoteImageDS : IImageDS {

    override fun saveImage(imageUrl: String): Single<String> {
        return loadFullSizeImage(imageUrl)
                .flatMap {
                    saveImageToStorage(it)
                }
                .subscribeOn(Schedulers.io())
    }

    private fun loadFullSizeImage(imageUrl: String): Single<Pair<String, Bitmap>> {
        return Single.create { e ->
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(imageUrl)
                    .build()
            var response: Response? = null

            try {
                response = client.newCall(request).execute()
            } catch (ex: Exception) {
                e.onError(ex)
            }

            try {
                val b = response!!.body!!.bytes()
                val bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
                e.onSuccess(System.currentTimeMillis().toString() + ".jpg" to bitmap)
            } catch (ex: Exception) {
                e.onError(ex)
            }
        }
    }

    private fun saveImageToStorage(imageToSave: Pair<String, Bitmap>): Single<String> {
        return Single.create { e ->
            val root = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera/"
            val myDir = File(root)
            val file = File(myDir, imageToSave.first)
            try {
                val out = FileOutputStream(file)
                imageToSave.second.compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
                out.close()
            } catch (ex: Exception) {
                e.onError(ex)
            }
            e.onSuccess(file.absolutePath)
        }
    }

}