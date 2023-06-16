package com.example.species

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchSpeciesAdapter(private val context: Context,  private val searchSpeciesList: List<SearchSpecies>) :
    RecyclerView.Adapter<SearchSpeciesAdapter.SearchSpeciesViewHolder>() {

    var onItemClick : ((SearchSpecies) -> Unit)? = null

    class SearchSpeciesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val searchSpeciesName : TextView = itemView.findViewById(R.id.searchSpeciesName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSpeciesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_search_item, parent, false)
        return SearchSpeciesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchSpeciesList.size
    }

    override fun onBindViewHolder(holder: SearchSpeciesViewHolder, position: Int) {
        val searchSpecies = searchSpeciesList[position]
        holder.searchSpeciesName.text = searchSpecies.searchSpeciesName

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(searchSpecies)
        }
    }
}
