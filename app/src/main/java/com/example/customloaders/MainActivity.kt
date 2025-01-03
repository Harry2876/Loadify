/**
 * Copyright 2024 Hariom Harsh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.customloaders

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customloaders.data.model.LoaderItem
import com.example.customloaders.databinding.ActivityMainBinding


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
//        val loaderList = listOf(
//            LoaderItem("Circular Loader", com.android.ezy_loader.ui.CircularLoaderView(this).apply {
//                setCircleColor(Color.BLUE)
//                setCircleSize(150f)
//                setCircleStroke(16f)
//                setCircleSpeed(600)
//            }),
//            LoaderItem("Line Spinner", com.android.ezy_loader.ui.LineSpinnerView(this).apply {
//                setLoaderColor(Color.BLUE)
//                setLoaderSize(100f)
//                setLoaderStroke(10f)
//                setLoaderSpeed(500)
//            }),
//            LoaderItem("Tail Chase Spinner", com.android.ezy_loader.ui.TailChaseSpinner(this).apply {
//                setSpinnerColor(Color.GREEN)
//                setDotSize(0.2f)
//                setSpinnerSize(120f)
//            }),
//            LoaderItem("Three Dots Pyramid", com.android.ezy_loader.ui.ThreeDotsPyramid(this).apply {
//                setDotColor(Color.GREEN)
//                setPyramidSize(200f)
//            }),
//            LoaderItem("Dot Pulse", com.android.ezy_loader.ui.DotPulse(this).apply {
//                setDotColor(Color.MAGENTA)
//                setDotCount(3)
//                setDotSize(30f)
//                setDotSpacing(2f)
//            }),
//        )
//
//        //setting up the recycler view
//        binding.recyclerView.layoutManager = GridLayoutManager(this,2)
//        binding.recyclerView.adapter = LoaderAdapter(loaderList)
//
//
    }
}