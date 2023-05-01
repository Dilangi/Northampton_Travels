package com.example.northamptontravels.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.northamptontravels.R
import com.example.northamptontravels.adapter.ReviewAdapter
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {
    var toggle: ActionBarDrawerToggle?=null
    var drawerLayout: DrawerLayout?=null
    var navView: NavigationView?=null
    var rvReviews: RecyclerView?=null
    var actPackage: AutoCompleteTextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rvReviews = findViewById(R.id.rvReviews)
        actPackage = findViewById(R.id.actPackage)
        drawerLayout=findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
        drawerLayout?.addDrawerListener(toggle!!)
        toggle!!.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView?.setNavigationItemSelectedListener {
            setMenu(it.itemId)
            true
        }

//set the package names to autoCompleteTextView
        val packages = resources.getStringArray(R.array.tourPackage)
        val packageAdapter = ArrayAdapter<String>(this, R.layout.review,packages)
        actPackage?.setAdapter(packageAdapter)

//        val userAdapter = ReviewAdapter(dbHelper.getUsers())
//
//        rvReviews.layoutManager = LinearLayoutManager(this)
//        rvReviews.adapter = userAdapter
    }

    //set navigation drawermenu
    private fun setMenu(itemId: Int) {
        when(itemId){
            R.id.addReview-> {
                //direct to Addd Review page
                val intent = Intent(this, AddReviewActivity::class.java)
                startActivity(intent)
            }
            R.id.myReviews-> {
                //todo set reviews list by reviews of user
//                    setMyReviews()
            }
            R.id.reviews-> {
                //todo set all posted reviews for particular package
//                    setReviews()
            }
            R.id.editProfile-> {
                //direct to Profile Activity
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.logout-> {
                //todo confirmation and logout
//                    showDialog()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle!!.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}