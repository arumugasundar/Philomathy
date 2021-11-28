package com.example.philomathyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.philomathyapp.databinding.ActivityAppBinding
import com.example.philomathyapp.fragments.FavouritesFragment
import com.example.philomathyapp.fragments.HomeFragment
import com.example.philomathyapp.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth

class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val homeFragment = HomeFragment();
    private val profileFragment = ProfileFragment();
    private val favouritesFragment = FavouritesFragment();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_app)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        setCurrentFragment(homeFragment)



        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> setCurrentFragment(homeFragment)
                R.id.ic_favourite -> setCurrentFragment(favouritesFragment)
                R.id.ic_profile -> setCurrentFragment(profileFragment)
                R.id.ic_logout -> logout()
            }
            true
        }


    }

    private fun logout() {
        firebaseAuth.signOut()
        checkUser()
    }

    private fun setCurrentFragment(fragment: Fragment) {
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.FragmentContainer,fragment)
            transaction.commit()
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            startActivity(Intent(this@AppActivity ,LoginActivity::class.java))
            finish()
        }
    }
}