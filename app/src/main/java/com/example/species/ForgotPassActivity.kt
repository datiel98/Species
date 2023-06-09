package com.example.species

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.species.databinding.ActivityForgotPassBinding
import com.example.species.databinding.ActivitySignInBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendEmailButton.setOnClickListener {
            val emailReset = binding.forgotEmail.text.toString()

            if (emailReset.isNotEmpty()) {
                if (isValidEmail(emailReset)) {
                    Firebase.auth.sendPasswordResetEmail(emailReset)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@ForgotPassActivity, "Email Sent", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Incorrect Email Format", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}