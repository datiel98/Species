package com.example.species

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.species.databinding.ActivityListActicleBinding
import com.example.species.databinding.ActivityListSpeciesBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListActicleBinding
    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var articlesList: ArrayList<Articles>

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var img_uri: Uri? = null

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

        binding.home.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        binding.profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
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
}