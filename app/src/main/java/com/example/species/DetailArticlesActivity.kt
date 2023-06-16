package com.example.species

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.species.databinding.ActivityDetailArticlesBinding
import com.example.species.databinding.ActivityDetailSpeciesBinding

class DetailArticlesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticlesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eachArticles = intent.getParcelableExtra<Articles>("articles")
        if (eachArticles != null) {
            val authorTextView: TextView = findViewById(R.id.detailArticlesAuthor)
            val dateTextView: TextView = findViewById(R.id.detailArticlesDate)
            val titleTextView: TextView = findViewById(R.id.detailArticlesTitle)
            val descriptionTextView: TextView = findViewById(R.id.detailArticlesDescription)
            val articlesImageView: ImageView = findViewById(R.id.detailArticlesImageView)
            val authorImageView: ImageView = findViewById(R.id.authorImageView)

            authorTextView.text = eachArticles.author
            dateTextView.text = eachArticles.date
            titleTextView.text = eachArticles.title
            descriptionTextView.text = eachArticles.description

            Glide.with(this)
                .load(eachArticles.img_url)
                .into(articlesImageView)

            Glide.with(this)
                .load(eachArticles.author_img)
                .into(authorImageView)

        }
    }
}