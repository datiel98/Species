package com.example.species

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.species.databinding.ActivityDetailSpeciesBinding


class DetailSpeciesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSpeciesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSpeciesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eachSpecies = intent.getParcelableExtra<Species>("species")
        if (eachSpecies != null) {
            val plantName: TextView = findViewById(R.id.detailPlantName)
            val kingdomTextView: TextView = findViewById(R.id.detailKingdomTextView)
            val familyTextView: TextView = findViewById(R.id.detailFamilyTextView)
            val descriptionTextView: TextView = findViewById(R.id.detailDescriptionTextView)
            val eachSpeciesImageView: ImageView = findViewById(R.id.detailImageView)

            plantName.text = eachSpecies.name
            kingdomTextView.text = eachSpecies.kingdom
            familyTextView.text = eachSpecies.family
            descriptionTextView.text = eachSpecies.description
            Glide.with(this)
                .load(eachSpecies.img_url)
                .into(eachSpeciesImageView)
        }
    }
}