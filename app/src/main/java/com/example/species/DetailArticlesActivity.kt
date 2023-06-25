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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.species.databinding.ActivityDetailArticlesBinding
import com.example.species.databinding.ActivityDetailSpeciesBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailArticlesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticlesBinding
    private lateinit var likedArticlesList: ArrayList<String>

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var img_uri: Uri? = null
    var liked = false

    lateinit var preference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        likedArticlesList = arrayListOf()

        preference = getSharedPreferences("User", Context.MODE_PRIVATE)
        val email = preference.getString("email", null)

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

        var userName = ""

        FirebaseFirestore.getInstance().collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    userName = document.getString("name").toString()
                    likedArticlesList = document.get("favorite_articles") as ArrayList<String>
                }
                if (likedArticlesList.contains(eachArticles?.title)) {
                    liked = true
                    binding.likeCardView.setCardBackgroundColor(Color.RED)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show()
            }

        binding.likeCardView.setOnClickListener {
            liked = !liked
            if (liked) {
                binding.likeCardView.setCardBackgroundColor(Color.RED)
                Firebase.firestore.collection("users").document(userName).update("favorite_articles", FieldValue.arrayUnion(eachArticles?.title))
            } else {
                binding.likeCardView.setCardBackgroundColor(Color.GRAY)
                Firebase.firestore.collection("users").document(userName).update("favorite_articles", FieldValue.arrayRemove(eachArticles?.title))
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