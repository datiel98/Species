package com.example.species

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PhotographyAdapter (private val photographyList : List<Photography>) :
    RecyclerView.Adapter<PhotographyAdapter.PhotographyViewHolder>() {

        class PhotographyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val photographyImageView : ImageView = itemView.findViewById(R.id.photographyImageView)
            val photographyTextView : TextView = itemView.findViewById(R.id.photographyTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotographyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.each_photography, parent, false)
            return PhotographyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return photographyList.size
        }

        override fun onBindViewHolder(holder: PhotographyViewHolder, position: Int) {
            val plantType = photographyList[position]
            holder.photographyImageView.setImageResource(plantType.photographyImage)
            holder.photographyTextView.text = plantType.photographyText
        }
}