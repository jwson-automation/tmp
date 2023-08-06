package com.project.tmp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.project.tmp.databinding.ActivityLoginBinding

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Google Login
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        var getLoginResponse =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                Log.d(TAG, "resultCode: ${it.resultCode}")

                if (it.resultCode == RESULT_OK) {
                    Log.d(TAG, "onCreate: 로그인성공!")
                    val HomeIntent = Intent(this, HomeActivity::class.java)
                    startActivity(HomeIntent)
                } else {
                    Log.d(TAG, "onCreate: 로그인실패!")
                    Toast.makeText(this, "구글 로그인이 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

        // Initialize Firebase Auth instance
        auth = FirebaseAuth.getInstance()

        // Initialize the views from XML
        val usernameEditText: EditText = findViewById(R.id.editTextUsername)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)

        // Set click listener for the login button
        binding.LoginBtn.setOnClickListener {
            // Get the input values from the EditText views
            val username: String = usernameEditText.text.toString()
            val password: String = passwordEditText.text.toString()

            // Check if the username and password are not empty
            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Call the login function with the entered username and password
                login(username, password)
            } else {
                // Show a toast message if the fields are empty
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.GoogleLoginBtn.setOnClickListener() {
            val signInIntent = googleSignInClient.signInIntent
            getLoginResponse.launch(signInIntent)
        }

        binding.SignUpBtn.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }

    private fun login(username: String, password: String) {
        // Sign in with the provided username and password using Firebase Auth
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful, show a success message
                    Toast.makeText(this, "Login successful for user: $username", Toast.LENGTH_SHORT)
                        .show()
                    val HomeIntent = Intent(this, HomeActivity::class.java)
                    startActivity(HomeIntent)
                } else {
                    // Login failed, show an error message
                    Toast.makeText(
                        this,
                        "Login failed. Please check your credentials.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}