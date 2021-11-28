package com.example.philomathyapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Toast
//import androidx.appcompat.app.ActionBar
import com.example.philomathyapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding:ActivityLoginBinding
    // ActionBar
//    private lateinit var actionBar: ActionBar
    // ProgressDialog
    private lateinit var progressDialog: ProgressDialog
    // FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionbar
//        actionBar = supportActionBar!!
//        actionBar.title = "Login"

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.noAccountTv.setOnClickListener {
            val intent = Intent(this@LoginActivity,SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener{
            validateData()
        }
    }

    private fun validateData(){
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.error = "Invalid email format"
        } else if(TextUtils.isEmpty(password)){
            binding.passwordEt.error = "Please enter password"
        } else {
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"LoggedIn as $email", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@LoginActivity,AppActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this,"Login failed due to ${ e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser(){
        var firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            startActivity(Intent(this,AppActivity::class.java))
            finish()
        }
    }
}