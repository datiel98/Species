package com.example.species

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlantTypesAdapter(private val plantTypeList : List<PlantTypes>) :
    RecyclerView.Adapter<PlantTypesAdapter.PlantTypeViewHolder>() {

    class PlantTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val plantTypesImageView : ImageView = itemView.findViewById(R.id.plantTypesImageView)
        val plantTypesTextView : TextView = itemView.findViewById(R.id.plantTypesTextView)
        val plantNumberTextView : TextView = itemView.findViewById(R.id.plantNumberTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantTypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_plant_type, parent, false)
        return PlantTypeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return plantTypeList.size
    }

    override fun onBindViewHolder(holder: PlantTypeViewHolder, position: Int) {
        val plantType = plantTypeList[position]
        holder.plantTypesImageView.setImageResource(plantType.plantTypesImage)
        holder.plantTypesTextView.text = plantType.plantTypesName
        holder.plantNumberTextView.text = plantType.plantTypesNum
    }
}