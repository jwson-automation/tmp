package com.project.tmp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.tmp.databinding.ActivitySignUpBinding

private const val TAG = "SignUpActivity"
class SignUpActivity : AppCompatActivity() {
    // Initialize Firebase Auth and Firestore instances
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Firestore instances
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize the views from XML
        val usernameEditText: EditText = findViewById(R.id.editTextUsername)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val signupButton: Button = findViewById(R.id.buttonSignup)

        // Set click listener for the signup button
        signupButton.setOnClickListener {
            // Get the input values from the EditText views
            val username: String = usernameEditText.text.toString()
            val password: String = passwordEditText.text.toString()

            // Check if the username and password are not empty
            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Call the signup function with the entered username and password
                signup(username, password)
            } else {
                // Show a toast message if the fields are empty
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signup(username: String, password: String) {
        // Create a new user in Firebase Auth with the provided username and password
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Get the Firebase Auth user
                    val user = auth.currentUser

                    // Add the user data to Firestore
                    user?.let {
                        val userData = hashMapOf(
                            "username" to username,
                            "email" to it.email
                        )

                        // Specify the collection path where user data will be stored (e.g., "users")
                        firestore.collection("users")
                            .document(it.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                // Show a success message if data is added successfully
                                Toast.makeText(this, "Signup successful for user: $username", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener { e ->
                                // Show an error message if data addition fails
                                Toast.makeText(this, "Error adding user data: $e", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Show an error message if the user creation in Firebase Auth fails
                    Toast.makeText(this, "Error creating user: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}