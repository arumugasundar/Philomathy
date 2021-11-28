package com.example.philomathyapp

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.io.File

import com.example.philomathyapp.databinding.ActivityLoginBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import androidx.appcompat.app.ActionBar
import com.example.philomathyapp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivityProfileBinding
    //ActionBar
//    private lateinit var actionBar: ActionBar
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.getImage.setOnClickListener {

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Fetching Image...")
            progressDialog.setCancelable(false)
            progressDialog.show()


            val imageName = binding.etImageId.text.toString()
            val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName.png")

            val localfile = File.createTempFile("tempImage",".png")
            storageRef.getFile(localfile).addOnSuccessListener {

                if(progressDialog.isShowing) progressDialog.dismiss()

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imageView.setImageBitmap(bitmap)
            }.addOnFailureListener{
                if(progressDialog.isShowing) progressDialog.dismiss()

                Toast.makeText(this,"Failed to retrieve the image",Toast.LENGTH_SHORT).show();
            }

        }

        //configure ActionBar
//        actionBar = supportActionBar!!
//        actionBar.title = "Profile"

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click, logout
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            val email = firebaseUser.email
            //binding.emailTv.text = email
        }else{
            startActivity(Intent(this@ProfileActivity ,LoginActivity::class.java))
            finish()
        }
    }
}