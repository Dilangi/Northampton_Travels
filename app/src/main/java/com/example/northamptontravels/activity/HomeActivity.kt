package com.example.northamptontravels.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
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


class HomeActivity : AppCompatActivity(){

    var getPackageReviews = "${Constant.REVIEW_URL}${Constant.PACKAGE_REVIEWS}"

    var toggle: ActionBarDrawerToggle? = null
    var drawerLayout: DrawerLayout? = null
    var navView: NavigationView? = null
    var btnReview: AppCompatButton? = null
    var rvReviews: RecyclerView? = null
    var ivSearch: ImageView?= null
    var spPackage: Spinner? = null
    var packagesName: String? = null
    var reviewList: ArrayList<Review> = ArrayList()
//    var packagePosition: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    ivSearch= findViewById(R.id.ivSearch)
        btnReview = findViewById(R.id.btnReview)
        rvReviews = findViewById(R.id.rvReviews)
        spPackage = findViewById(R.id.spPackage)

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


        //set action listener for tap on button
        btnReview!!.setOnClickListener {
            //direct to Addd Review page
            val intent = Intent(this, AddReviewActivity::class.java)
            startActivity(intent)
        }
    val packageList =resources.getStringArray(R.array.tourPackage)
    val spAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,packageList)
    spPackage?.adapter=spAdapter


        //listener for the spinner
        spPackage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                packagesName= adapterView!!.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                packagesName=null
            }}
    //set action listener for tap on image view
    ivSearch!!.setOnClickListener {
        if(packagesName!=packageList[0])
            getReview()
    }
        }

    //HTTP request to get comments for given package
    private fun getReview() {
        reviewList= ArrayList()
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method.POST,
            getPackageReviews,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    if(obj.get("error")==false){
                    val objArray = obj.getJSONArray("review") //extract data array from json string

                    //get review from json array and add into arrayList
                    for (i in 0..objArray.length()-1) {
                        val gson = Gson()
                        val review: Review =
                            gson.fromJson(objArray.getJSONObject(i).toString(), Review::class.java)
                        reviewList.add(review)
                    }

                    }
                    else{
                            Toast.makeText(this,obj.get("message").toString(),Toast.LENGTH_SHORT).show()
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
                params.put("packagesName", packagesName!!)
                return params
            }
        }

        queue.add(stringRequest)
    }

    private fun setReview() {
        val commentsAdapter = ReviewAdapter(reviewList, true)
        rvReviews!!.layoutManager = LinearLayoutManager(this)
        rvReviews!!.adapter = commentsAdapter
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