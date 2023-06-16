package com.example.species

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.species.databinding.ActivityAddSpeciesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddSpeciesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSpeciesBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var storageRef: StorageReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSpeciesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val capturedImage = intent.getParcelableExtra<Uri>("image")
        if (capturedImage != null) {
            val displayCapturedImage: ImageView = findViewById(R.id.detailArticlesImageView)

            displayCapturedImage.setImageURI(capturedImage)
        }

        binding.home.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        binding.profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        storageRef = FirebaseStorage.getInstance().reference.child("Plants")
        firebaseFirestore = FirebaseFirestore.getInstance()


        binding.materialCardView.setOnClickListener {
            val plantName = binding.nameEditText.text.toString()
            val kingdomEditText = binding.kindomEditText.text.toString()
            val familyEditText = binding.familyEditText.text.toString()
            val descriptionEditText = binding.descriptionEditText.text.toString()
            val speciesEditText = binding.speciesEditText.text.toString()
            val rating = binding.ratingEditText.text.toString()

            binding.progressBar.visibility = View.VISIBLE
            storageRef = storageRef.child(System.currentTimeMillis().toString())
            capturedImage?.let {
                storageRef.putFile(it).addOnCompleteListener {task ->
                    if (task.isSuccessful) {

                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            val map = hashMapOf(
                                "name" to plantName,
                                "kingdom" to kingdomEditText,
                                "family" to familyEditText,
                                "description" to descriptionEditText,
                                "species" to speciesEditText,
                                "img_url" to uri.toString(),
                                "rating" to rating
                            )

                            firebaseFirestore.collection("plants").add(map).addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful){
                                    Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()
                                }
                                binding.progressBar.visibility = View.GONE
                            }
                        }

                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }

        }

    }

}