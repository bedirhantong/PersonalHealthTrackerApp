package com.example.personalhealthtracker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.data.HealthyActivity

class HealthyActivityAdapter(private val healthyActivityList : ArrayList<HealthyActivity>) : RecyclerView.Adapter<HealthyActivityAdapter.HealthyActivityHolder>(){


    class HealthyActivityHolder(itemView : View ) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthyActivityHolder {
        // recyclerView ile onun içine koyacağımız elemanları birbirine bağlar, yani inflator kullanacağız
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_view_activity,parent,false)
        return HealthyActivityHolder(view)
    }

    override fun getItemCount(): Int {
        // recyclerView içerisinde kaç item gözüksün
        return healthyActivityList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HealthyActivityHolder, position: Int) {
        if (holder.itemView.findViewById<TextView>(R.id.recycler_view_activity_type).text == "Running Activity"){
            holder.itemView.findViewById<TextView>(R.id.recycler_view_duration_view).text =
                healthyActivityList[position].timeElapsed
            holder.itemView.findViewById<TextView>(R.id.recycler_view_activity_type).text =
                healthyActivityList[position].activityName
            holder.itemView.findViewById<TextView>(R.id.recycler_view_road_travelled_view).text =
                healthyActivityList[position].roadTravelled
            holder.itemView.findViewById<TextView>(R.id.recycler_view_kcal_view).text =
                healthyActivityList[position].energyCons
        }else{
            // if it is step counting
            holder.itemView.findViewById<TextView>(R.id.recycler_view_elapsedTimeText).text = "Step"
            holder.itemView.findViewById<TextView>(R.id.recycler_view_duration_view).text =
                healthyActivityList[position].timeElapsed
            holder.itemView.findViewById<TextView>(R.id.recycler_view_activity_type).text =
                healthyActivityList[position].activityName
            holder.itemView.findViewById<TextView>(R.id.recycler_view_road_travelled_view).text =
                healthyActivityList[position].roadTravelled
            holder.itemView.findViewById<TextView>(R.id.recycler_view_kcal_view).text =
                healthyActivityList[position].energyCons
        }




    }
}