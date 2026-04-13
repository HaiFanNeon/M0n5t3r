package com.example.myapplication.manager

import android.graphics.Bitmap
import com.example.myapplication.`interface`.BitmapExportStrategy
import com.example.myapplication.`interface`.BitmapLoaderStrategy
import com.example.myapplication.`interface`.BitmapSaveStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class BitmapFileManager(
    private val saveStrategy: BitmapSaveStrategy,
    private val loadStrategy: BitmapLoaderStrategy,
    private val exportStrategy: BitmapExportStrategy
    ) {
    suspend fun saveDraft(bitmap: Bitmap, matrix: FloatArray): Result<Unit> = withContext(Dispatchers.IO) {
        saveStrategy.saveDraft(bitmap, matrix)
    }

    fun loadDraft(): Pair<Bitmap, FloatArray>? {
        return loadStrategy.loadDraft()
    }

    suspend fun exportDraft(bitmap: Bitmap) : Result<Unit> = withContext(Dispatchers.IO) {
        exportStrategy.export(bitmap)
    }

}