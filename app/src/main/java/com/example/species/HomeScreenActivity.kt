package com.example.species

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.example.species.databinding.ActivityHomeScreenBinding
import com.example.species.databinding.ActivitySignInBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var plantTypesList:ArrayList<PlantTypes>
    private lateinit var plantTypesAdapter: PlantTypesAdapter

    private lateinit var photographyList:ArrayList<Photography>
    private lateinit var photographyAdapter: PhotographyAdapter

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var img_uri: Uri? = null

    lateinit var preference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = getSharedPreferences("User", Context.MODE_PRIVATE)

        val email = preference.getString("email", null)
        var userName: String? = null
        var userImage: String? = null
        val editor = preference.edit()

        if (email != null) {
            FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        userName = document.get("name") as String
                        userImage = document.get("image") as String
                    }
                    if (userName != null) {
                        val displayUserName: TextView = findViewById(R.id.textUserName)
                        val str = "Hello $userName,"
                        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
                        displayUserName.text = str

                        editor.putString("name", userName)
                        editor.apply()
                    }
                    if (userImage != null) {
                        val displayUserImage: ImageView = findViewById(R.id.authorImageView)
                        Glide.with(this)
                            .load(userImage)
                            .into(displayUserImage)

                        editor.putString("image", userImage)
                        editor.apply()
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show()
                }

        }


        initPlantTypes()
        initPhotography()

        binding.species.setOnClickListener {
            val intent = Intent(this, SearchSpeciesActivity::class.java)
            startActivity(intent)
        }

        binding.articles.setOnClickListener {
            val intent = Intent(this, ListArticleActivity::class.java)
            startActivity(intent)
        }

        binding.profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.materialCardView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    openCamera()
                }
            } else {
                openCamera()
            }
        }

        binding.addingNew.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED
                ) {
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    openCamera()
                }
            } else {
                openCamera()
            }
        }


    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")
        img_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, img_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intent = Intent(this, AddSpeciesActivity::class.java)
        intent.putExtra("image", img_uri)
        startActivity(intent)
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