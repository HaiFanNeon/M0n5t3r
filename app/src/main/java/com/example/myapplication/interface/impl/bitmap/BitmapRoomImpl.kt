package com.example.myapplication.`interface`.impl.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.myapplication.`interface`.BitmapLoaderStrategy
import com.example.myapplication.`interface`.BitmapSaveStrategy

class BitmapRoomImpl(
    private val context: Context
) : BitmapSaveStrategy, BitmapLoaderStrategy {
    override suspend fun saveDraft(
        bitmap: Bitmap,
        matrix: FloatArray
    ): Result<Unit> {
        Log.i("testtest", "room saveDraft")
        return Result.success(Unit)
    }

    override fun loadDraft(): Pair<Bitmap, FloatArray>? {
        Log.i("testtest", "room loadDraft")
        return null
    }
}