package com.example.northamptontravels.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.northamptontravels.R
import com.example.northamptontravels.adapter.ReviewAdapter
import com.example.northamptontravels.entity.Review
import com.example.northamptontravels.utils.Constant
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MyReviewsActivity : AppCompatActivity(), ReviewAdapter.OnItemClickListener {

    var getMyReviews = "${Constant.REVIEW_URL}${Constant.MY_REVIEWS}"

    var toggle: ActionBarDrawerToggle? = null
    var drawerLayout: DrawerLayout? = null
    var btnReview: AppCompatButton? = null
    var navView: NavigationView? = null
    var rvReviews: RecyclerView? = null
    var reviewList: ArrayList<Review> = ArrayList()
    var author: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reviews)

        btnReview = findViewById(R.id.btnReview)
        rvReviews = findViewById(R.id.rvReviews)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout?.addDrawerListener(toggle!!)
        toggle!!.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView?.setNavigationItemSelectedListener {
            setMenu(it.itemId)
            true
        }

        //set action listener for tap on button
        btnReview!!.setOnClickListener {
            //direct to Addd Review page
            val intent = Intent(this, AddReviewActivity::class.java)
            startActivity(intent)
        }

        val preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        author = preferences.getString("username", "")

        getReview()
    }

    //HTTP request to get comments where author = username
    private fun getReview() {

        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method.POST,
            getMyReviews,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    val objArray: JSONArray =
                        obj.getJSONArray("reviews") //extract data array from json string

                    //get review from json array and add into arrayList
                    for (i in 0..objArray.length()) {
                        val gson = Gson()
                        val review: Review =
                            gson.fromJson(objArray.getJSONObject(i).toString(), Review::class.java)
                        reviewList.add(review)
                    }

                    setReview()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    if (error != null) {
                        Toast.makeText(
                            applicationContext,
                            error.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("author", author!!)
                return params
            }
        }

        queue.add(stringRequest)
    }

    private fun setReview() {
        val commentsAdapter = ReviewAdapter(reviewList, false,this)
        rvReviews!!.layoutManager = LinearLayoutManager(this)
        rvReviews!!.adapter = commentsAdapter
    }

    //set navigation drawermenu
    private fun setMenu(itemId: Int) {
        when (itemId) {
            R.id.addReview -> {
                //direct to Add Review page
                val intent = Intent(this, AddReviewActivity::class.java)
                startActivity(intent)
            }
            R.id.myReviews -> {
                Toast.makeText(this,resources.getText(R.string.alreadyHere), Toast.LENGTH_SHORT).show()
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

    override fun onItemClick(position: Int) {
        var review: Review = reviewList[position]
        //direct to Review Activity
        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra("review", review)//pass review details to the next activity
        startActivity(intent)
    }
}