package com.example.myapplication.config

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.manager.BitmapFileManager
import com.example.myapplication.viewModel.DrawingViewModel

class DrawingViewModelFactory(
    private val bitmapFileManager: BitmapFileManager,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DrawingViewModel(bitmapFileManager) as T
    }
}