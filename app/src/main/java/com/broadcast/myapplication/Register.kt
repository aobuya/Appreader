package com.broadcast.myapplication

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextVerifyPassword: EditText
    private lateinit var btnSignin: Button
    private lateinit var btnRegister: Button //btnSign
    private lateinit var progressDialog: ProgressDialog

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()
        val currentuser = FirebaseAuth.getInstance().currentUser
        if(currentuser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextVerifyPassword = findViewById(R.id.editTextVerifyPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnSignin = findViewById(R.id.btnSignIn)

        progressDialog = ProgressDialog(this) // Initialize ProgressDialog

        // Enable password visibility toggle
        editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        editTextVerifyPassword.transformationMethod = PasswordTransformationMethod.getInstance()

        btnRegister.setOnClickListener {
            registerUser()
        }
        btnSignin.setOnClickListener{

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        val togglePassword = findViewById<Button>(R.id.togglePassword)

        togglePassword.setOnClickListener {

                // Show password
                editTextPassword.transformationMethod = null
                editTextVerifyPassword.transformationMethod = null

            }

    }

    private fun registerUser() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val verifyPassword = editTextVerifyPassword.text.toString()

        // Check if passwords match
        if (password != verifyPassword) {
            // Show an error message
            editTextVerifyPassword.error = "Passwords do not match"
            return
        }

        // Show the progress dialog
        progressDialog.setMessage("Registering...")
        progressDialog.show()


        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()

                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser

                    // Save user registration state using SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("registered", true)
                    editor.apply()

                    // Navigate to the main activity
                    navigateToMainActivity()
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error occurred"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the registration activity
    }
}