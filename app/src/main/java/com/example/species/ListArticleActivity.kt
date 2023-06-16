package com.example.species

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.species.databinding.ActivityListActicleBinding
import com.example.species.databinding.ActivityListSpeciesBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListActicleBinding
    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var articlesList: ArrayList<Articles>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListActicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listArticleRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListArticleActivity)
        }

        articlesList = arrayListOf()

        FirebaseFirestore.getInstance().collection("articles")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val eachArticles: Articles? = document.toObject<Articles>(Articles::class.java)
                    articlesList.add(eachArticles!!)
                }
                articlesAdapter = ArticlesAdapter(this, articlesList)
                binding.listArticleRecyclerView.adapter = articlesAdapter
                articlesAdapter.onItemClick = {
                    val intent = Intent(this, DetailArticlesActivity::class.java)
                    intent.putExtra("articles", it)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show()
            }
    }
}