package com.example.customloaders.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customloaders.R
import com.example.customloaders.data.model.LoaderItem

class LoaderAdapter(private val loaderList: List<LoaderItem>) :
    RecyclerView.Adapter<LoaderAdapter.LoaderViewHolder>() {

    class LoaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val loaderContainer: FrameLayout = view.findViewById(R.id.loaderContainer)
        val loaderName: TextView = view.findViewById(R.id.loaderName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loader, parent, false)
        return LoaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoaderViewHolder, position: Int) {
        val loaderItem = loaderList[position]

        // Add the loader view to the container
        holder.loaderContainer.removeAllViews() // Clear any existing views
        holder.loaderContainer.addView(loaderItem.loaderView)

        // Set the loader name
        holder.loaderName.text = loaderItem.name
    }

    override fun getItemCount(): Int = loaderList.size
}
