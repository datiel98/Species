package com.example.species

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.species.databinding.ActivityHomeScreenBinding
import com.example.species.databinding.ActivityListSpeciesBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ListSpeciesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListSpeciesBinding
    private lateinit var speciesAdapter: SpeciesAdapter
    private lateinit var speciesList: ArrayList<Species>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListSpeciesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listSpeciesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListSpeciesActivity)
        }

        speciesList = arrayListOf()

        FirebaseFirestore.getInstance().collection("plants")
            .whereEqualTo("species", "Cactus")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val eachSpecies: Species? = document.toObject<Species>(Species::class.java)
                    speciesList.add(eachSpecies!!)
                }
                speciesAdapter = SpeciesAdapter(this, speciesList)
                binding.listSpeciesRecyclerView.adapter = speciesAdapter
                speciesAdapter.onItemClick = {
                    val intent = Intent(this, DetailSpeciesActivity::class.java)
                    intent.putExtra("species", it)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show()
            }


    }

}