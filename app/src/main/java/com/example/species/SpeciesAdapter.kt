package com.example.species

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide

class SpeciesAdapter(private val context: Context, var species: List<Species>): RecyclerView.Adapter<SpeciesViewHolder>() {

    var onItemClick : ((Species) -> Unit)? = null
    override fun getItemCount(): Int {
        return species.size
    }

    fun setFilterdList(species: List<Species>) {
        this.species = species
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeciesViewHolder {
        return SpeciesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.each_species, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SpeciesViewHolder, position: Int) {
        val eachSpecies = species[position]

        holder.plantName.text = eachSpecies.name
        holder.kingdomTextView.text = eachSpecies.kingdom
        holder.familyTextView.text = eachSpecies.family
        holder.descriptionTextView.text = eachSpecies.description

        Glide.with(context)
            .load(eachSpecies.img_url)
            .into(holder.eachSpeciesImageView)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(eachSpecies)
        }
    }
}

class SpeciesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val plantName: TextView = itemView.findViewById(R.id.plantName)
    val kingdomTextView: TextView = itemView.findViewById(R.id.kingdomTextView)
    val familyTextView: TextView = itemView.findViewById(R.id.familyTextView)
    val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    val eachSpeciesImageView: ImageView = itemView.findViewById(R.id.eachSpeciesImageView)

}