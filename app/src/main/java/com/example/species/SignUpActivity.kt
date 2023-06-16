package com.example.species

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.species.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


const val TAG = "FIRESTORE"
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val userName = binding.userName.text.toString()
            val userImage = ""
            var favoriteSpecies: ArrayList<String>
            var favoriteArticles: ArrayList<String>

            favoriteSpecies = arrayListOf()
            favoriteArticles = arrayListOf()

            if (email.isNotEmpty() && pass.isNotEmpty() && userName.isNotEmpty()) {
                if (isValidEmail(email)) {
                    if (isValidPassword(pass)) {
                        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(this, "Signed Up Successful", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, SignInActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this,
                                        it.exception.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }

                        val user = hashMapOf(
                            "email" to email,
                            "password" to pass,
                            "name" to userName,
                            "image" to userImage,
                            "favorite_species" to favoriteSpecies,
                            "favorite_articles" to favoriteArticles
                        )
                        db.collection("users").document(userName)
                            .set(user)
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                    } else {
                        Toast.makeText(this, "Password must have at least 11 characters, number, lowercase, uppercase and special characters", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Incorrect Email Format", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        if (password.length < 11) return false
        if (password.filter { it.isDigit() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false

        return true
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}