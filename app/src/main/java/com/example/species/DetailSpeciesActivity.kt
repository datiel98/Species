package com.example.species

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.species.databinding.ActivityDetailSpeciesBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DetailSpeciesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSpeciesBinding
    private lateinit var likedSpeciesList: ArrayList<String>

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var img_uri: Uri? = null
    var liked = false

    lateinit var preference : SharedPreferences

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSpeciesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        likedSpeciesList = arrayListOf()

        preference = getSharedPreferences("User", Context.MODE_PRIVATE)
        val email = preference.getString("email", null)

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

        var userName = ""

        FirebaseFirestore.getInstance().collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    userName = document.getString("name").toString()
                    likedSpeciesList = document.get("favorite_species") as ArrayList<String>
                }
                if (likedSpeciesList.contains(eachSpecies?.name)) {
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
                Firebase.firestore.collection("users").document(userName).update("favorite_species", FieldValue.arrayUnion(eachSpecies?.name))
            } else {
                binding.likeCardView.setCardBackgroundColor(Color.GRAY)
                Firebase.firestore.collection("users").document(userName).update("favorite_species", FieldValue.arrayRemove(eachSpecies?.name))
            }
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