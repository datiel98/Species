package com.example.species

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.species.databinding.ActivityHomeScreenBinding
import com.example.species.databinding.ActivitySearchSpeciesBinding
import com.google.firebase.firestore.FirebaseFirestore


class SearchSpeciesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchSpeciesBinding
    private lateinit var searchSpeciesList: ArrayList<SearchSpecies>
    private lateinit var searchSpeciesAdapter: SearchSpeciesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchSpeciesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchSpeciesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchSpeciesActivity)
        }
        searchSpeciesList = arrayListOf()

        FirebaseFirestore.getInstance().collection("plants")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val eachSpecies = document.getString("species")
                    eachSpecies?.let { SearchSpecies(it) }?.let { searchSpeciesList.add(it) }
                }
                searchSpeciesList = searchSpeciesList.distinct() as ArrayList<SearchSpecies>
                searchSpeciesAdapter = SearchSpeciesAdapter(this, searchSpeciesList)
                binding.searchSpeciesRecyclerView.adapter = searchSpeciesAdapter
                searchSpeciesAdapter.onItemClick = {
                    val intent = Intent(this, ListSpeciesActivity::class.java)
                    intent.putExtra("searchSpecies", it)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show()
            }
    }




}