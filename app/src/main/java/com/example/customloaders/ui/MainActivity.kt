package com.example.customloaders.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customloaders.R
import com.example.customloaders.data.model.LoaderItem
import com.example.customloaders.databinding.ActivityMainBinding
import com.example.customloaders.ui.adapter.LoaderAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //setting up loaders data
        val loaderList = listOf(
            LoaderItem("Circular Loader", CircularLoaderView(this).apply {
                setCircleColor(Color.BLUE)
                setCircleSize(150f)
                setCircleStroke(16f)
                setCircleSpeed(600)
            }),
            LoaderItem("Line Spinner", LineSpinnerView(this).apply {
                setLoaderColor(Color.BLUE)
                setLoaderSize(100f)
                setLoaderStroke(10f)
                setLoaderSpeed(500)
            }),
            LoaderItem("Tail Chase Spinner", TailChaseSpinner(this).apply {
                setSpinnerColor(Color.GREEN)
                setDotSize(0.2f)
                setSpinnerSize(120f)
            }),
            LoaderItem("Three Dots Pyramid", ThreeDotsPyramid(this).apply {
                setDotColor(Color.GREEN)
                setPyramidSize(200f)
            }),
            LoaderItem("Dot Pulse", DotPulse(this).apply {
                setDotColor(Color.MAGENTA)
                setDotCount(3)
                setDotSize(30f)
                setDotSpacing(2f)
            }),
        )

        //setting up the recycler view
        binding.recyclerView.layoutManager = GridLayoutManager(this,2)
        binding.recyclerView.adapter = LoaderAdapter(loaderList)


    }
}