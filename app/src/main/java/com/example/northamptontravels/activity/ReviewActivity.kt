package com.example.northamptontravels.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.northamptontravels.R
import com.example.northamptontravels.R.string.overall
import com.example.northamptontravels.entity.Review
import com.google.android.material.navigation.NavigationView
import java.io.Serializable

class ReviewActivity : AppCompatActivity() {

    var toggle: ActionBarDrawerToggle? = null
    var drawerLayout: DrawerLayout? = null
    var navView: NavigationView? = null

    var ratingBar: AppCompatRatingBar?= null
    var ratingFood: AppCompatRatingBar?= null
    var ratingTravel: AppCompatRatingBar?= null
    var ratingAccommodation: AppCompatRatingBar?= null
    var tvMonth: TextView?= null
    var tvAuthor: TextView?= null
    var tvPosted: TextView?= null
    var tvPackage: TextView?= null
    var tvReview: TextView?= null
    var tvFood: TextView?= null
    var tvTransport: TextView?= null
    var tvAccommodation: TextView?= null
    var ivPhotos: ImageView?= null
    var btnBack: AppCompatButton?= null
    var btnNext: AppCompatButton?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
//todo add author field and hide to author else visible
        //todo consider admin email and keep shared preference admin true
        btnBack= findViewById(R.id.btnBack)
        btnNext= findViewById(R.id.btnNext)

        //set drawer navigation
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navView)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout?.addDrawerListener(toggle!!)
        toggle!!.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView?.setNavigationItemSelectedListener {
            setMenu(it.itemId)
            true
        }

        tvPackage = findViewById(R.id.tvPackage)
        tvMonth = findViewById(R.id.tvMonth)
        tvPosted = findViewById(R.id.tvPosted)
        tvReview = findViewById(R.id.tvReview)
        tvFood = findViewById(R.id.tvFood)
        tvTransport = findViewById(R.id.tvTransport)
        tvAccommodation = findViewById(R.id.tvAccommodation)
        tvAuthor = findViewById(R.id.tvAuthor)
        ratingBar = findViewById(R.id.ratingBar)
        ratingFood = findViewById(R.id.ratingFood)
        ratingTravel = findViewById(R.id.ratingTravel)
        ratingAccommodation = findViewById(R.id.ratingAccommodation)


        val reviewObj= intent.getParcelableExtra<Review>("review")
        if(reviewObj!=null)
            setReview(reviewObj)
        else
            Toast.makeText(this,resources.getString(R.string.close),Toast.LENGTH_SHORT).show()

        //set action listener for tap on button
        btnNext!!.setOnClickListener {
            //direct to Addd Review page
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        btnBack!!.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setReview(review: Review) {
        tvPackage!!.text=review.packagesName
        ratingBar!!.rating= review.overallRating
        ratingFood!!.rating=review.foodRating
        ratingTravel!!.rating=review.transportRating
        ratingAccommodation!!.rating=review.accommodationRating
        tvMonth!!.text=review.visitedDate
        tvPosted!!.text=review.postedDate
        tvAuthor!!.text=review.author
        tvReview!!.text=review.overall
        tvAccommodation!!.text=review.accommodation
        tvFood!!.text=review.food
        tvTransport!!.text=review.transport
    }


    //set navigation drawermenu
    private fun setMenu(itemId: Int) {
        when (itemId) {
            R.id.addReview -> {
                //direct to Addd Review page
                val intent = Intent(this, AddReviewActivity::class.java)
                startActivity(intent)
            }
            R.id.myReviews -> {
                //direct to MyReviews page
                val intent = Intent(this, MyReviewsActivity::class.java)
                startActivity(intent)
            }
            R.id.reviews -> {
                //todo set all posted reviews for particular package, make this home
//                    setReviews()
            }
            R.id.editProfile -> {
                //direct to Profile Activity
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                //todo confirmation and logout
//                    showDialog()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle!!.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}