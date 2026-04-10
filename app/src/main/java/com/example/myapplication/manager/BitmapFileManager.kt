package com.example.myapplication.manager

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.os.Build
import android.provider.MediaStore
import android.util.JsonReader
import android.util.Log
import com.example.myapplication.`interface`.Coordinates
import com.example.myapplication.model.DrawingModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class BitmapFileManager(private val context: Context) {

    suspend fun saveDraft(bitmap: Bitmap, matrix: FloatArray): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try{
            val file = File(context.filesDir, "test.png")
            val gson = Gson()
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
            val json = gson.toJson(matrix)
            val jsonFile = File(context.filesDir, "coordinates.json")
            FileOutputStream(jsonFile).use { fos ->
                fos.write(json.toByteArray(Charsets.UTF_8))
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun loadDraft(): Pair<Bitmap, FloatArray>? {
        val file = File(context.filesDir, "test.png")
        if (!file.exists()) {
            Log.i("testtest", "first open app")
            return null
        }

        val json = File(context.filesDir, "coordinates.json")
        if (!json.exists()) {
            return null
        }
        var values = FloatArray(9) {0f}
        values[0] = 1f
        values[4] = 1f
        values[8] = 1f
        val jsonString = json.readText(Charsets.UTF_8)
        val prase = Gson().fromJson(jsonString, FloatArray::class.java)
        if (prase != null && prase.size == 0) {
            values = prase
        }
        return Pair(BitmapFactory.decodeFile(file.absolutePath), values)
    }

    suspend fun exportDraft(bitmap: Bitmap) : Result<Unit> = withContext(Dispatchers.IO){
        val exportBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(exportBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        val filename = "${System.currentTimeMillis()}.png"
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/draw")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            } else {
                val path = android.os.Environment.getExternalStoragePublicDirectory(
                    android.os.Environment.DIRECTORY_PICTURES
                ).absolutePath + "draw"
                java.io.File(path).mkdirs()
                put(MediaStore.Images.Media.DATA, "${path}/${filename}")
            }
        }
        try {
            // 3. 插入数据库并获取输出流
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?: throw Exception("Failed to create new MediaStore record.")

            resolver.openOutputStream(uri)?.use { fos ->
                exportBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}