package com.example.northamptontravels.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.northamptontravels.R
import com.example.northamptontravels.entity.Review
import com.example.northamptontravels.fragment.FirstFragment
import com.example.northamptontravels.fragment.FourthFragment
import com.example.northamptontravels.fragment.SecondFragment
import com.example.northamptontravels.fragment.ThirdFragment
import com.example.northamptontravels.utils.Constant
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddReviewActivity : AppCompatActivity() {

    var addReview = "${Constant.REVIEW_URL}${Constant.INSERT_REVIEW}"

    var  btnNext: AppCompatButton? = null
    var  btnBack: AppCompatButton? = null
    var toggle: ActionBarDrawerToggle?=null
    var drawerLayout: DrawerLayout?=null
    var navView: NavigationView?=null
    var ivAll: ImageView?=null
    var ivFood: ImageView?=null
    var ivAccommodation: ImageView?=null
    var ivTransport: ImageView?=null

    val fragment1 = FirstFragment()
    val fragment2 = SecondFragment()
    val fragment3 = ThirdFragment()
    val fragment4 = FourthFragment()
    var fragment: Fragment? = fragment1
    lateinit var reviewData: Review
    val bundle = Bundle()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        ivAll = findViewById(R.id.ivAll)
        ivTransport = findViewById(R.id.ivTransport)
        ivFood = findViewById(R.id.ivFood)
        ivAccommodation = findViewById(R.id.ivAccommodation)
    btnNext = findViewById(R.id.btnNext)
    btnBack = findViewById(R.id.btnBack)
    drawerLayout=findViewById(R.id.drawerLayout)
    navView = findViewById(R.id.navView)

    toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
    drawerLayout?.addDrawerListener(toggle!!)
    toggle!!.syncState()

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    navView?.setNavigationItemSelectedListener {
        setMenu(it.itemId)
        true
    }

    setFragment()
    btnNext!!.setOnClickListener{
        getFragment()
    }
    btnBack!!.setOnClickListener{
        supportFragmentManager.beginTransaction().apply {
            backPressed()
        }
    }

    }

    //set previous Fragment and change selected imageview on button click
    private fun backPressed() {
        if (fragment == fragment4) {
            fragment = fragment3
            ivAccommodation!!.setBackgroundResource(R.drawable.bg_btn_navigate)
            ivTransport!!.setBackgroundResource(R.drawable.grey_btn)
            btnNext!!.text = resources.getString(R.string.next)
        }else if(fragment == fragment3) {
            fragment = fragment2
            ivFood!!.setBackgroundResource(R.drawable.bg_btn_navigate)
            ivAccommodation!!.setBackgroundResource(R.drawable.grey_btn)
        }else if(fragment == fragment2) {
            fragment = fragment1
            ivAll!!.setBackgroundResource(R.drawable.bg_btn_navigate)
            ivFood!!.setBackgroundResource(R.drawable.grey_btn)
        }
        onBackPressed()
    }

    //set next Fragment and change selected imageview on button click
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getFragment(){

        if (fragment == fragment1) {
            fragment = fragment2
            ivFood!!.setBackgroundResource(R.drawable.bg_btn_navigate)
            ivAll!!.setBackgroundResource(R.drawable.grey_btn)

            //get data from fragment and set into next fragment
            reviewData = fragment1.getFirstReviewData()
            bundle.putParcelable("data",reviewData)
            fragment2.arguments = bundle

        }else if(fragment == fragment2) {
            fragment = fragment3
            ivAccommodation!!.setBackgroundResource(R.drawable.bg_btn_navigate)
            ivFood!!.setBackgroundResource(R.drawable.grey_btn)

            //get data from fragment and set into next fragment
            reviewData = fragment2.getSecondReviewData()
            bundle.putParcelable("data",reviewData)
            fragment3.arguments = bundle
        }else if(fragment == fragment3) {
            fragment = fragment4
            ivTransport!!.setBackgroundResource(R.drawable.bg_btn_navigate)
            ivAccommodation!!.setBackgroundResource(R.drawable.grey_btn)
            btnNext!!.text = resources.getString(R.string.submit)

            //get data from fragment and set into next fragment
            reviewData = fragment3.getThirdReviewData()
            bundle.putParcelable("data",reviewData)
            fragment4.arguments = bundle
        }else if(fragment == fragment4){
            reviewData = fragment4.getForthReviewData()

            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val formattedDate = currentDate.format(formatter)
            reviewData.postedDate = formattedDate

            val preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            reviewData.author = preferences.getString("username", "")!!

            //request for HTTP call
            addReview()
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flReview, fragment!!)
            addToBackStack(null)
            commit()
        }
    }

    //insert review to the database
    private fun addReview() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method. POST,
            addReview,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    Toast.makeText(this, obj.get("message").toString(), Toast.LENGTH_LONG).show()
                    nextView()
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
                params.put("packageName", reviewData.packageName!!)
                params.put("author", reviewData.author!!)
                params.put("overall", reviewData.overall!!)
                params.put("overallRating", reviewData.overallRating!!.toString())
                params.put("food", reviewData.food!!)
                params.put("foodRating", reviewData.foodRating!!.toString())
                params.put("transport", reviewData.transport!!)
                params.put("transportRating", reviewData.transportRating!!.toString())
                params.put("accommodation", reviewData.accommodation!!)
                params.put("accommodationRating", reviewData.accommodationRating!!.toString())
                params.put("postedDate", reviewData.postedDate!!)
                params.put("visitedDate", reviewData.visitedDate!!)
                return params
            }
        }

        queue.add(stringRequest)
    }

    //direct to Login page
    private fun nextView() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    //set navigation drawer menu
    private fun setMenu(itemId: Int) {
        when(itemId){
            R.id.addReview-> {
                //direct to Addd Review page
                Toast.makeText(this, "Already there", Toast.LENGTH_SHORT).show()
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


    // set the first fragment into frame layout
    private fun setFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flReview, fragment1)
            commit()
        }
        ivAll!!.setBackgroundResource(R.drawable.bg_btn_navigate)
    }


}


