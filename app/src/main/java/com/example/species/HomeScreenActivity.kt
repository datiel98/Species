package com.example.species

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.species.databinding.ActivityHomeScreenBinding
import com.example.species.databinding.ActivitySignInBinding

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var plantTypesList:ArrayList<PlantTypes>
    private lateinit var plantTypesAdapter: PlantTypesAdapter

    private lateinit var photographyList:ArrayList<Photography>
    private lateinit var photographyAdapter: PhotographyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlantTypes()
        initPhotography()
    }

    private fun initPlantTypes() {
        binding.recyclerView?.setHasFixedSize(true)
        binding.recyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val snapHelper : SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerView)
        plantTypesList = ArrayList()

        addDataPlantTypes()
        plantTypesAdapter = PlantTypesAdapter(plantTypesList)
        binding.recyclerView?.adapter = plantTypesAdapter
    }

    private fun addDataPlantTypes() {
        plantTypesList.add(PlantTypes(R.drawable.homeplants, "Home Plants", "68 Types of Plants"))
        plantTypesList.add(PlantTypes(R.drawable.hugeplants, "Huge Plants", "100 Types of Plants"))
        plantTypesList.add(PlantTypes(R.drawable.hangingplants, "Hanging Plants", "10 Types of Plants"))
    }

    private fun initPhotography() {
        binding.photographyRecyclerView?.setHasFixedSize(true)
        binding.photographyRecyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        val snapHelper : SnapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(binding.photographyRecyclerView)
        photographyList = ArrayList()

        addDataPhotography()
        photographyAdapter = PhotographyAdapter(photographyList)
        binding.photographyRecyclerView?.adapter = photographyAdapter
    }

    private fun addDataPhotography() {
        photographyList.add(Photography(R.drawable.photo1, "#Mini"))
        photographyList.add(Photography(R.drawable.photo2, "#Homely"))
        photographyList.add(Photography(R.drawable.photo3, "#Cute"))
        photographyList.add(Photography(R.drawable.photo5, "#Colorfull"))
        photographyList.add(Photography(R.drawable.photo6, "#Sharply"))

    }
}