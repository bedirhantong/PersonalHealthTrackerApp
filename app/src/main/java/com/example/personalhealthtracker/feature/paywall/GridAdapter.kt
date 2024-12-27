package com.example.personalhealthtracker.feature.paywall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalhealthtracker.R

class GridAdapter(private val imageList: List<Int>) :
    BaseInfiniteAdapter<Int, GridAdapter.ViewHolder>(imageList) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val realPosition = getRealPosition(position)
        holder.imageView.setImageResource(imageList[realPosition])
    }
}


abstract class BaseInfiniteAdapter<T, VH : RecyclerView.ViewHolder>(
    private val dataList: List<T>
) : RecyclerView.Adapter<VH>() {

    override fun getItemCount(): Int = Int.MAX_VALUE

    protected fun getRealPosition(position: Int): Int = position % dataList.size
}




