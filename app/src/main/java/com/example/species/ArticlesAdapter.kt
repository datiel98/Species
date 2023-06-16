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

class ArticlesAdapter(private val context: Context, val articles: List<Articles>): RecyclerView.Adapter<ArticlesViewHolder>() {

    var onItemClick : ((Articles) -> Unit)? = null
    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return ArticlesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.each_article, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val eachArticles = articles[position]

        holder.authorTextView.text = eachArticles.author
        holder.dateTextView.text = eachArticles.date
        holder.titleTextView.text = eachArticles.title

        Glide.with(context)
            .load(eachArticles.img_url)
            .into(holder.articlesImageView)

        Glide.with(context)
            .load(eachArticles.author_img)
            .into(holder.authorImageView)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(eachArticles)
        }
    }
}

class ArticlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
    val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    val articlesImageView: ImageView = itemView.findViewById(R.id.articlesImageView)
    val authorImageView: ImageView = itemView.findViewById(R.id.authorImageView)

}