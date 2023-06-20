package com.example.personalhealthtracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.data.DateOfAct

class DateOfActAdapter(private val dateOfActList : ArrayList<DateOfAct>):  RecyclerView.Adapter<DateOfActAdapter.DateOfActHolder>() {
    private var listener: OnItemClickListener? = null

    class DateOfActHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(dateOfAct: DateOfAct, position: Int) {
            itemView.findViewById<TextView>(R.id.dateButton).text = dateOfAct.date

//            // Öğe tıklandığında pozisyonu bildir
//            itemView.setOnClickListener {
//                listener?.onItemClick(position)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateOfActHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_view_date,parent,false)
        return DateOfActHolder(view)
    }

    override fun getItemCount(): Int {
        return dateOfActList.size
    }

    override fun onBindViewHolder(holder: DateOfActHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.dateButton).text =
            dateOfActList[position].date

        val dateOfAct = dateOfActList[position]
        holder.bind(dateOfAct, position)
    }

    // Tıklama olay dinleyicisi arayüzü
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }




}