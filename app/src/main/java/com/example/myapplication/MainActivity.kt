package com.example.myapplication
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ext.copyFrom
import com.example.myapplication.manager.BitmapFileManager
import com.example.myapplication.model.DrawingModel
import com.example.myapplication.view.DrawingView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


class MainActivity : ComponentActivity(), View.OnClickListener{

    private lateinit var drawingView: DrawingView
    private lateinit var bitmapFileManager: BitmapFileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        drawingView = findViewById<DrawingView>(R.id.drawingView)
        bitmapFileManager = BitmapFileManager(this)
        loadDraw()
        drawingView.onSaveBitmapListener = {
            save()
        }
        findViewById<Button>(R.id.btnEarse).setOnClickListener(this)
        findViewById<Button>(R.id.btnBrush).setOnClickListener(this)
        findViewById<Button>(R.id.btnClear).setOnClickListener(this)
        findViewById<Button>(R.id.btnExport).setOnClickListener(this)
        findViewById<Button>(R.id.btnPreview).setOnClickListener(this)
    }

    private fun save() {
        val bp = drawingView.getBitmap()
        val cdt = drawingView.gettReviewCoordinates()
        lifecycleScope.launch{
            bitmapFileManager.saveDraft(bp, cdt)
        }
    }

    private fun loadDraw() {
        val loadDraft = bitmapFileManager.loadDraft()
        if (loadDraft != null) {

            drawingView.setBitmap(loadDraft.first)
            drawingView.settRealCoordinates(loadDraft.second)
            drawingView.settReviewCoordinates(loadDraft.second)
        }

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnEarse -> {
                drawingView.setEraseMode()
            }
            R.id.btnBrush -> {
                drawingView.setBrushMode()
            }
            R.id.btnClear -> {
                lifecycleScope.launch { drawingView.clear() }
            }
            R.id.btnExport -> {
                lifecycleScope.launch {
                    val result = bitmapFileManager.exportDraft(drawingView.getBitmap())
                    result.onSuccess {
                    }.onFailure { exception ->
                        Log.i("testtest", exception.message.toString())
                    }
                }
            }
            R.id.btnPreview -> {
                drawingView.setReview()
            }
        }
    }
}