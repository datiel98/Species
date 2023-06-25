package com.example.species

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.species.databinding.ActivityHomeScreenBinding
import com.example.species.databinding.ActivityProfileBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    lateinit var preference : SharedPreferences

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var storageRef: StorageReference

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var img_uri: Uri? = null
    var userName: String? = null
    var userImage: String? = null

    private lateinit var speciesAdapter: SpeciesAdapter
    private lateinit var speciesList: ArrayList<Species>
    private lateinit var likedSpeciesList: List<*>

    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var articlesList: ArrayList<Articles>
    private lateinit var likedArticlesList: List<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        likedSpeciesList = listOf("")
        likedArticlesList = listOf("")

        binding.likedSpeciesRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.likedArticlesRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)


        preference = getSharedPreferences("User", Context.MODE_PRIVATE)
        userName = preference.getString("name", null)
        userImage = preference.getString("image", null)
        binding.userNameTextView.text = userName
        val editor = preference.edit()
        val email = preference.getString("email", null)

        val displayUserImage: ImageView = findViewById(R.id.authorImageView)
        Glide.with(this)
            .load(userImage)
            .into(displayUserImage)

        binding.home.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        binding.logOutImageView.setOnClickListener {
            editor.putString("email", null)
            editor.apply()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.avatarCardView.setOnClickListener {
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

        FirebaseFirestore.getInstance().collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    userName = document.getString("name").toString()
                    likedSpeciesList = document.get("favorite_species") as List<*>
                    likedArticlesList = document.get("favorite_articles") as List<*>
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show()
            }

        speciesList = arrayListOf()
        articlesList = arrayListOf()
        FirebaseFirestore.getInstance().collection("plants")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val eachSpecies: Species = document.toObject<Species>(Species::class.java)
                    if (likedSpeciesList.contains(eachSpecies?.name)) {
                        speciesList.add(eachSpecies)
                    }
                }
                speciesAdapter = SpeciesAdapter(this, speciesList)
                binding.likedSpeciesRecyclerView.adapter = speciesAdapter
                speciesAdapter.onItemClick = {
                    val intent = Intent(this, DetailSpeciesActivity::class.java)
                    intent.putExtra("species", it)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show()
            }

        FirebaseFirestore.getInstance().collection("articles")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val eachArticles: Articles = document.toObject<Articles>(Articles::class.java)
                    if (likedArticlesList.contains(eachArticles?.title)) {
                        articlesList.add(eachArticles)
                    }
                }
                articlesAdapter = ArticlesAdapter(this, articlesList)
                binding.likedArticlesRecyclerView.adapter = articlesAdapter
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
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.authorImageView.setImageURI(img_uri)

        storageRef = FirebaseStorage.getInstance().reference.child("Users")
        firebaseFirestore = FirebaseFirestore.getInstance()

        storageRef = storageRef.child(System.currentTimeMillis().toString())
        img_uri?.let {
            storageRef.putFile(it).addOnCompleteListener {task ->
                if (task.isSuccessful) {

                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imgUrl = uri.toString()
                        userName?.let { it1 ->
                            firebaseFirestore.collection("users")
                                .document(it1)
                                .update("image", imgUrl)
                        }
                    }

                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}